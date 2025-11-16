package com.example.datenight.exception;

/*
 * DaoException is a custom exception class specifically for handling errors in the DAO (Data Access Object) layer.
 *
 * Key points about this class:
 *
 * 1. Custom Exception:
 *    - This class allows us to create exceptions with meaningful messages that describe what went wrong
 *      in the database layer (e.g., failed insert, connection issues).
 *    - It provides two constructors: one for a message and one for a message plus a cause.
 *
 * 2. Extends RuntimeException:
 *    - By extending RuntimeException, DaoException is an **unchecked exception**.
 *    - Unchecked means:
 *        • The compiler does NOT require methods to declare "throws DaoException".
 *        • Callers do NOT have to handle it with try/catch if they don’t want to.
 *    - This is useful in DAOs because many database errors are unexpected or unrecoverable, and we don’t
 *      want to force higher layers to handle every possible SQL exception individually.
 *
 * 3. Inheritance:
 *    - This class demonstrates **inheritance**, an OOP concept where a subclass can reuse code from a superclass.
 *    - DaoException inherits all behavior from RuntimeException (and its parent classes Exception and Throwable):
 *        • getMessage() – retrieves the error message
 *        • getCause() – retrieves the original exception that triggered this one
 *        • printStackTrace() – prints the full stack trace for debugging
 *        • toString() – returns a string representation of the exception
 *    - Because of inheritance, we do not need to implement any of these methods ourselves.
 *
 * 4. Constructors:
 *    - DaoException(String message):
 *        • Allows creation of an exception with a custom error message.
 *        • Calls super(message) to pass the message up to RuntimeException, which handles storing it internally.
 *
 *    - DaoException(String message, Exception cause):
 *        • Allows creation of an exception with both a custom message and a cause (another exception).
 *        • Calls super(message, cause) to store both the message and the underlying exception.
 *        • This is helpful for **wrapping low-level exceptions** (e.g., SQLException) and passing them up the call stack
 *          while preserving debugging information.
 *
 * 5. Benefits:
 *    - Provides consistent exception handling for the DAO layer.
 *    - Makes it easier for higher layers (service, controller) to handle database errors in a standardized way.
 *    - Preserves detailed debugging information without rewriting exception handling logic.
 */
public class DaoException extends RuntimeException {
    //The below allows us to create a custom error message
    //super(message) calls the constructor of Runtime Exception to store the custom message.

    public DaoException(String message) {
        super(message);
    }

    // Constructor that allows us to provide a custom message and the original exception that caused this error
    // Calls the superclass constructor to store both the message and the cause
    public DaoException(String message, Exception cause){
        super(message, cause);
    }
}
