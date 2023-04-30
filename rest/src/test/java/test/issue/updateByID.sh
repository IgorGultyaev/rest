#!/bin/bash


curl -v -X PUT -H "Content-Type: application/json" -d '{"id": 1, "name": "updated", "content": "content", "closed": true}' http://localhost:8080/api/issues


