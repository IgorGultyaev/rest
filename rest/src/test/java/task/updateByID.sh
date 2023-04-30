#!/bin/bash


curl -v -X PUT -H "Content-Type: application/json" -d '{"id": 1, "name": "updated", "deadline": "2023-04-28T00:00:00Z", "done": true}' http://localhost:8080/api/issues


