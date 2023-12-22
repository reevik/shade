<br/>

[![CircleCI](https://dl.circleci.com/status-badge/img/circleci/MijSkLN7jgimc5d7X5xmw1/Cme7KtQEisA8WcpiPBhMCP/tree/main.svg?style=svg&circle-token=7010818212d0b87edb68ac4e0ad06609d52f7426)](https://dl.circleci.com/status-badge/redirect/circleci/MijSkLN7jgimc5d7X5xmw1/Cme7KtQEisA8WcpiPBhMCP/tree/main)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

The **Darkest** is a shadow and dark testing framework. Shadow testing enables development teams to carry out endpoint switches of service dependencies in a
graceful way by calling both endpoints, say, A and B, and comparing their API responses. You can simply add the following maven dependency to your project: 

```xml
    <dependency>
      <groupId>net.reevik</groupId>
      <artifactId>darkest</artifactId>
      <version>0.1.0</version>
    </dependency>
```

## Usage

You can consider the following code snippet how the routing configuration and router instance are created: 

```java
    RoutingConfiguration<String> routingConfiguration=Builder.<String>create()
    .withSideA(()->"A")
    .withSideB(EndpointRouterTest::assertNotCalled)
    .withResultValidator(mustEqual)
    .withRoutingCriterion(countingCriterion)
    .withRoutingMode(RoutingMode.A_SIDE)
    .build();
    
    EndpointRouter<String> router = new EndpointRouter<>(routingConfiguration);
    String result=router.route();
```

The **RoutingConfiguration** takes two commands which implement the integration logic with the A and B endpoints. The **RoutingConfiguration** instance requires a validator which is used to validate the response objects from A and B endpoints, whereas a routing criterion to decide when the B side needs to 
be called. The **Routing Mode** defines the operating mode of the EndpointRouter. A_SIDE is used to route all requests to the A endpoint (the existing integration) and B_SIDE works like A_SIDE, but this time all requests will be routed to the B endpoint (the new integration). **SHADOW_MODE_PASSIVE** results in calling both endpoints simultaneously. If the A-B validation succeeds, in other words A and B endpoint's results are compatible, the **EndpointRouter** returns the result object from the A side, **SHADOW_MODE_ACTIVE**, in case the A-B validation succeeds, it returns the result object from the B side.

## Bugs and Feedback

For bugs, questions and discussions please use
the [GitHub Issues](https://github.com/notingolmo/darkest/issues).

 
## LICENSE

Copyright 2023 Erhan Bagdemir

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

[license]:LICENSE-2.0.txt
[license img]:https://img.shields.io/badge/License-Apache%202-blue.svg

