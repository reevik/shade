<br/>

[![CircleCI](https://dl.circleci.com/status-badge/img/circleci/MijSkLN7jgimc5d7X5xmw1/Cme7KtQEisA8WcpiPBhMCP/tree/main.svg?style=svg&circle-token=7010818212d0b87edb68ac4e0ad06609d52f7426)](https://dl.circleci.com/status-badge/redirect/circleci/MijSkLN7jgimc5d7X5xmw1/Cme7KtQEisA8WcpiPBhMCP/tree/main)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![License](https://img.shields.io/badge/JDK-21%20-green.svg)](https://github.com/reevik/shade/wiki/Java-Support)
[![License](https://img.shields.io/badge/Release-0.2.0%20-green.svg)](https://central.sonatype.com/artifact/net.reevik/shade)
[![Javadoc](https://img.shields.io/badge/Javadoc%20-green.svg)](https://reevik.github.io/shade/)

'Dark Launching' refers to feature enablement in production behind the scenes, like 'Shadow
Testing', which is the testing process of new application features in the background without making
them generally available. It aims to activate new software features before the GA without
interrupting the operations and being transparent to customers. During the testing phase, you are to
monitor, for example, whether the new integration works seamlessly with existing components under
realistic data and load. In services landscape, for instance, 'Shadow Testing' enables development
teams to execute endpoint switches of service dependencies in a graceful way by calling both
endpoints, say, A and B, and comparing their API responses.

Shade is a small library for Java, which allows you to test new application features in the
background. You can simply add the following maven dependency to your project:

```xml

<dependency>
  <groupId>net.reevik</groupId>
  <artifactId>shade</artifactId>
  <version>0.2.0</version>
</dependency>
```

**## Usage

You can consider the following code snippet, it is how the routing configuration and router instance
are instantiated:

```java
    RoutingConfiguration<String> routingConfiguration=Builder.<String>create()
    .withSideA(()-> serviceOld.call())
    .withSideB(()-> serviceNew.call())
    .withResultValidator(mustEqual())
    .withRoutingCriterion(countingCriterion)
    .withRoutingMode(RoutingMode.A_SIDE)
    .build();

    EndpointRouter<String> router=new EndpointRouter<>(routingConfiguration);
    String result=router.route();
```

The **RoutingConfiguration** takes two commands which implement the integration logic with the A and
B components. The **RoutingConfiguration** instance requires a validator which is used to validate
the resulting objects from A and B calls, whereas a routing criterion to decide when the B side
needs to
be called. You can configure the router, e.g., for every tenth requests to hit the B side. The
**Routing Mode** defines the operating mode of the EndpointRouter. A_SIDE is used to route all
requests to the A endpoint (the existing integration), which effectively enables A component, and
B_SIDE works like A_SIDE, but this time all requests will be routed to the B component (the new
integration). **SHADOW_MODE_PASSIVE** results in calling both endpoints simultaneously. If the A-B
validation succeeds, in other words A and B component calls' results are compatible, the
**EndpointRouter** returns the result object from the A side, **SHADOW_MODE_ACTIVE**, in case the
A-B validation succeeds, it returns the result object from the B side.

The modes which require both sides to get activates, like **SHADOW_MODE_PASSIVE**, leverage Java's
virtual threads. So, it is important to note that using synchronized blocks within the command
implementation may end up with pinned platform threads. Therefore, I encourage you to use,
ReentrantLocks instead.

### Validators

Result validation is used to evaluate the result objects from both integration so that the framework
can take action if two results are compatible or incompatible. Depending on the routing mode
selected, if both results are equal, the routed may return the result object from the new
integration. It means, the Shade can roll out the new feature
by activating the new integration if the validation passes.

Shade brings a few simple validators, which you can use
out-of-the-box. Let's take two of them, which, I presume, will be used mostly:

* [ValidatorFactory#mustEqual](https://reevik.github.io/shade/net/reevik/shade/validators/ValidatorFactory.html#mustEqual())
* [ValidatorFactory#mustPerformSimilar](https://reevik.github.io/shade/net/reevik/shade/validators/ValidatorFactory.html#mustPerformSimilar(java.time.Duration))

Anyway, the validators are not limited to those above. You can write your own validators depending
on your needs by extending the framework.

### Routing Criterion

Routing criteria are preconditions for the router to determine when to enable the new integration.
For example, the CountingCondition implements the logic which allows every n'th request to be routed
to the new integration. Like validators, the framework brings a few criteria implementation
out-of-the-box.

## Bugs and Feedback**

For bugs, questions and discussions please use
the [GitHub Issues](https://github.com/notingolmo/shade/issues).

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

