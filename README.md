
# trial-task

REST API service for saving and scoring quotes


## API Reference

#### User accounts requests

Request body for POST:
```javascript
{
    "username": string,
    "password": string,
    "email": string
}
```

| HTTP Verbs | Endpoints | Action |
| --- | --- | --- |
| POST | /users | Create new account |
| GET | /users/{id} | User info |
| GET | /users/{id}/quotes | All quotes from user |

#### Requests for quotes modifications

For creating/updating/deleting quotes field "user-id" must be provided in request header
Request body for POST:
```javascript
{
    "quote": string
}
```
Request body for PUT:
```javascript
{
    "id": int,
    "quote": string
}
```

| HTTP Method | Endpoint | Action |
| --- | --- | --- |
| POST | /quotes | Add new quote |
| PUT | /quotes/{id} | Update quote |
| DELETE | /quotes/{id} | Delete quote |

#### Requests for quotes view

| HTTP Method | Endpoint | Action |
| --- | --- | --- |
| GET | /quotes/{id} | Quote info |
| GET | /quotes/{id}/chart | Quote score history |
| GET | /quotes/top | Top 10 quotes |
| GET | /quotes/worst | Worst 10 quotes |
| GET | /quotes/random | Random quote |

#### Requests for quotes voting
| HTTP Method | Endpoint | Action |
| --- | --- | --- |
| POST | /quotes/{id}/upvote | Upvote |
| POST | /quotes/{id}/downvote | Downvote |


## Deployment with Docker

The Docker image of this project is available at [Docker Hub](https://hub.docker.com/r/tvoloshin/trial-task/).

Pull command:
```
docker pull tvoloshin/trial-task:latest
```

To deploy image locally save [trial-task.mv.db](https://github.com/tvoloshin/trial-task/blob/main/trial-task.mv.db) and [docker-compose.yml](https://github.com/tvoloshin/trial-task/blob/main/docker-compose.yml) and run:

```
docker-compose up
```

