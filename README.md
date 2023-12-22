<br/>
<img src="https://github.com/reevik/darkest/blob/main/media/jmux.png" width="130" />

[![CircleCI](https://dl.circleci.com/status-badge/img/circleci/MijSkLN7jgimc5d7X5xmw1/Cme7KtQEisA8WcpiPBhMCP/tree/main.svg?style=svg&circle-token=7010818212d0b87edb68ac4e0ad06609d52f7426)](https://dl.circleci.com/status-badge/redirect/circleci/MijSkLN7jgimc5d7X5xmw1/Cme7KtQEisA8WcpiPBhMCP/tree/main)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

Darkest is a shadow and dark testing framework. Shadow testing enables development teams to carry out endpoint switches of service dependencies in a
graceful way by calling both endpoints, say, A and B, and comparing their API responses. You can simply add the following maven dependency to your project: 

```xml
    <dependency>
      <groupId>net.reevik</groupId>
      <artifactId>darkest</artifactId>
      <version>0.1.0</version>
    </dependency>
```

## Usage

You can consider the following code snippet how the routing configuration and router are created: 

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

The RoutingConfiguration takes two commands which implement the integration logic with the A and B side, a validator which validates the results of A and B endpoints, routing criterion to decide when the B side should
be called, and the routing mode. Routing mode can be A_SIDE, which opens the A gate permanently, B_SIDE, SHADOW_MODE_PASSIVE, in case the A-B validation succeeds, it still returns the result from the A side, SHADOW_MODE_ACTIVE, in case the A-B validation succeeds, Darkest returns the result object from B side.

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

