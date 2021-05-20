# records

An implementation of a simple person-records service.

## Running

### Part I: Core

    $ lein with-profile main-core run random.*

Loads 300 records, 100 each in formats pipe-separated, csv,
space-separated; then prints them out, sorted by various criteria.

### Part II: REST API

    $ lein with-profile main-rest run random.*

Loads the same 300 records, and services REST endpoints on port 3000.

## Testing

### Part I: Core

    $ lein with-profile main-core test

### Part II: REST API

    $ lein with-profile main-rest run random.*
    $ lein with-profile main-rest test

This runs REST API tests against a running REST server.

## Directions for improvement

- Run the integration tests such that after a POST, we have
  original number of records + 1
 
