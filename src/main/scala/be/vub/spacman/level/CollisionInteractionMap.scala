package be.vub.spacman.level

import be.vub.spacman.board.BoardUnit

import scala.collection.immutable.HashMap
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * A map of possible collisions and their handlers.
  *
  * @author Michael de Jong
  * @author Jeroen Roosen
  */
object CollisionInteractionMap {

  /**
    * Handles the collision between two colliding parties.
    *
    * @author Michael de Jong
    */
  trait CollisionHandler[C1 <: BoardUnit, C2 <: BoardUnit] {
    /**
      * Handles the collision between two colliding parties.
      *
      * @param collider
      * The collider.
      * @param collidee
      * The collidee.
      */
    def handleCollision(collider: C1, collidee: C2): Unit
  }

  /**
    * An symmetrical copy of a collision hander.
    *
    * @author Michael de Jong
    */
  private class InverseCollisionHandler[C1 <: BoardUnit, C2 <: BoardUnit] private[level](/**
                                                                                 * The handler of this collision.
                                                                                 */
                                                                               val handler: CollisionInteractionMap.CollisionHandler[C2, C1])
    extends CollisionInteractionMap.CollisionHandler[C1, C2] {
    /**
      * Handles this collision by flipping the collider and collidee, making
      * it compatible with the initial collision.
      */
    override def handleCollision(collider: C1, collidee: C2): Unit = {
      handler.handleCollision(collidee, collider)
    }
  }

}

class CollisionInteractionMap() extends CollisionMap {
  /**
    * The collection of collision handlers.
    */
  final private var handlers = new mutable.HashMap[Class[_ <: BoardUnit], mutable.Map[Class[_ <: BoardUnit], CollisionInteractionMap.CollisionHandler[_ <: BoardUnit, _ <: BoardUnit]]]

  /**
    * Adds a two-way collision interaction to this collection, i.e. the
    * collision handler will be used for both C1 versus C2 and C2 versus C1.
    *
    * @param collider
    *          The collider type.
    * @param collidee
    *          The collidee type.
    * @param handler
    *          The handler that handles the collision.
    */
  def onCollision[C1 <: BoardUnit, C2 <: BoardUnit](collider: Class[C1], collidee: Class[C2], handler: CollisionInteractionMap.CollisionHandler[C1, C2]): Unit = {
    onCollision(collider, collidee, true, handler)
  }

  /**
    * Adds a collision interaction to this collection.
    *
    * @param collider
    *          The collider type.
    * @param collidee
    *          The collidee type.
    * @param symetric
    *          <code>true</code> if this collision is used for both
    *          C1 against C2 and vice versa;
    *          <code>false</code> if only for C1 against C2.
    * @param handler
    *          The handler that handles the collision.
    */
  def onCollision[C1 <: BoardUnit, C2 <: BoardUnit](collider: Class[C1], collidee: Class[C2], symetric: Boolean, handler: CollisionInteractionMap.CollisionHandler[C1, C2]): Unit = {
    addHandler(collider, collidee, handler)
    if (symetric) addHandler(collidee, collider, new CollisionInteractionMap.InverseCollisionHandler[C2, C1](handler))
  }

  /**
    * Adds the collision interaction..
    *
    * @param collider
    * The collider type.
    * @param collidee
    * The collidee type.
    * @param handler
    * The handler that handles the collision.
    */
  private def addHandler(collider: Class[_ <: BoardUnit], collidee: Class[_ <: BoardUnit], handler: CollisionInteractionMap.CollisionHandler[_ <: BoardUnit, _ <: BoardUnit]): Unit = {
    if (!handlers.contains(collider)) handlers.put(collider, new scala.collection.mutable.HashMap[Class[_ <: BoardUnit], CollisionInteractionMap.CollisionHandler[_ <: BoardUnit, _ <: BoardUnit]])
    val map = handlers.get(collider).get
    map.put(collidee, handler)
  }

  /**
    * Handles the collision between two colliding parties, if a suitable
    * collision handler is listed.
    * @param collider
    *          The collider.
    * @param collidee
    *          The collidee.
    */
  @SuppressWarnings(Array("unchecked")) def collide[C1 <: BoardUnit, C2 <: BoardUnit](collider: C1, collidee: C2): Unit = {
    val colliderKey = getMostSpecificClass(handlers, collider.getClass)
    if (colliderKey == null) return
    val map = handlers.get(colliderKey)
    val collideeKey = getMostSpecificClass(map.get, collidee.getClass)
    if (collideeKey == null) return
    val collisionHandler = map.get(collideeKey).asInstanceOf[CollisionInteractionMap.CollisionHandler[C1, C2]]
    if (collisionHandler == null) return
    collisionHandler.handleCollision(collider, collidee)
  }

  /**
    * Figures out the most specific class that is listed in the map. I.e. if A
    * extends B and B is listed while requesting A, then B will be returned.
    *
    * @param map
    * The map with the key collection to find a matching class in.
    * @param key
    * The class to search the most suitable key for.
    * @return The most specific class from the key collection.
    */
  private def getMostSpecificClass(map: mutable.Map[Class[_ <: BoardUnit], _], key: Class[_ <: BoardUnit]): Class[_ <: BoardUnit] = {
    val collideeInheritance = getInheritance(key)
    import scala.collection.JavaConversions._
    for (pointer <- collideeInheritance) {
      if (map.containsKey(pointer)) return pointer
    }
    null
  }

  /**
    * Returns a list of all classes and interfaces the class inherits.
    *
    * @param clazz
    * The class to create a list of super classes and interfaces
    * for.
    * @return A list of all classes and interfaces the class inherits.
    */
  @SuppressWarnings(Array("unchecked")) private def getInheritance(clazz: Class[_ <: BoardUnit]) = {
    val found = new ArrayBuffer[Class[_ <: BoardUnit]]
    found += clazz

    var index = 0
    while ( {
      found.size > index
    }) {
      val current = found(index)
      val superClass = current.getSuperclass
      if (superClass != null && classOf[Nothing].isAssignableFrom(superClass))
        found += superClass.asInstanceOf[Class[_ <: BoardUnit]]
      for (classInterface <- current.getInterfaces) {
        if (classOf[Nothing].isAssignableFrom(classInterface))
          found += classInterface.asInstanceOf[Class[_ <: BoardUnit]]
      }
      index += 1
    }
    found
  }
}
