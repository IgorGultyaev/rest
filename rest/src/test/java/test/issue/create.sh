#!/bin/bash


curl -v -X POST -H "Content-Type: application/json" -d '{"name": "[bug] issue", "content": "content"}' http://localhost:8080/api/issues
