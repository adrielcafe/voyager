# Faq

[iOS Swipe Back support](#ios-swipeback) { #ios-swipeback }

Voyager does not have a built in support for swipe back yet, we are not 100% conformable with all
solutions that have out there and we think we will better of using a community made solution by copying
to your code base and be able to change as you want your app to behave.

See this [issue](https://github.com/adrielcafe/voyager/issues/144) each for community build solutions.

Alternatively, we can also discuss in the future a community build solution using `NavigationController`
under the hood like [`compose-cupertino`](https://github.com/alexzhirkevich/compose-cupertino/blob/master/cupertino-decompose/src/iosMain/kotlin/io/github/alexzhirkevich/cupertino/decompose/UIKitChildren.kt#L192) have implemented for Decompose.

[Support for predictive back animations](#predictive-back) { #predictive-back }

Voyager does not have a built in support for predictive back yet, but as well as iOS Swipe Back, the
community have build extensions, and snippets with support, see
[#223](https://github.com/adrielcafe/voyager/issues/223) and [144](https://github.com/adrielcafe/voyager/issues/144).

[Support for result passing between screens](#result-passing) { #result-passing }

Voyager does not have a built in support for swipe back yet, we are not 100% conformable with all
solutions that have out there, we encourage to use community made solutions by copying to your
code base and being free to extend as your code base needs.

See [#128](https://github.com/adrielcafe/voyager/pull/128) comments for community solutions.

[Deeplink support](./deep-links.md)
