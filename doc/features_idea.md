# Analysis of the service status microservice

#Introduction

The objectives of the services status microservice is to provide informations about service availability. User should be able to add services to monitor, then our services will test every x times its availability.
If a service faces some issues, an incidents should be created (either autmatically or manually by the service's owner). People should be able to subscribe to services to be notified when a incident happen. It should be able to subscribe by email, webhook, rss, ...
User should be able to create 

A lots of services doing what we need already exists.
There is two categories of services. The one who automatically test user's services, and the ones where the user must manually create the incident.
Here is a list of the most popular tools I found:

Manual:

- [estatus.io](https://estatus.io)
- [status.io](https://status.io)
- [statuscast.com](https://www.statuscast.com/) (can integrate an automatic service)

Automatic:

- [Uptime Robot](https://uptimerobot.com) (Free)
- [Pingdom](https://www.pingdom.com/)
- [Service Uptime](https://www.serviceuptime.com)
- [Binary Canary](https://www.binarycanary.com/)

# Common Features

Here is a list of common features for a status monitoring service. This is only idea for features that will potentially be implemented during the semester.

## Component

A component is a services the user want to monitor. It might be a website, api server, smtp server, etc.

## Automatic status tests

The service automatically send request to a specified Components. Multiple type of services should be tested, including the following:

- http/https
- ping
- smtp
- ftp
- ...

Services are tested every _n_ minutes. This allowed to have statistics about services' availibility. Response time is also an interesting data to monitor.

## Incident creation

When a services faces some issues, a incident can be created (automatically or not). A note describing the reasons and state of the incidents can be add by the component owner.
It should be possible do define the level of incident (service unavailable, slow services, partially available, etc..)

## Maintenance

It should be able to schedule a maintenance of a component to notify user about it.

## Subscription / notification

User should be able to subscribe to components. Then, when a components has some issue, every subscriber is notified about it. Subscription should be available by email, webhook, slack, post to an api, post to twitter account, etc...

## Data presentation

As components are constantly monitored, line charts of their availability and response time for the _n_ last days should be available on the status page.

## Custom status page

Status page can be customize (css/html) by every custom to match their company' design. The content of the status page should also be customizable.
For example, having the possibility to choose if a components status is public or private might be usefull.
