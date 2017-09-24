package be.vub.spacman

/**
  * Exception that is thrown when JPacman cannot be properly loaded from its resources.
  *
  * @author Arie van Deursen, 2014
  * @param message The exception message
  * @param cause The root cause (optional)
  */
class PacmanConfigurationException (message: String, cause: Throwable = null) extends Exception {}