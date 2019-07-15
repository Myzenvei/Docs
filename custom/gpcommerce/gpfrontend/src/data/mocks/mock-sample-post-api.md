FORMAT: 1A

# Sample POST API

Sample POST API Description

## Save Details [POST /sample/saveDetails]

- Request

* Headers

  content-type: application/json

  - Body
    {
    "name":"Sample",
    "id":1,
    "description":"Sample Description"
    }

- Response 200 (application/json)
  - Body
    {
    "response":true
    }
