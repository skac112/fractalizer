# fractalizer
Library for creating vector graphics by transforming simple shapes into complex ones.
Uses <a href="https://github.com/skac112/miro">miro</a>.

<strong>Nodes</strong> are used to transforming graphics into... more graphics:

graphics -> Node -> graphics

Node is essentialy a function which processes graphics. You can build intricate, fractal-like vector graphics starting from simple shape like circle or rectangle and processing it using nodes.

Processing is done in two "channels": <strong>proc channel<strong> and <strong>draw channel<strong>. Most of work is done in a proc channel. Draw channel is essentially a kind of stack where subsequent nodes add graphics which should be drawn and not later processed.

The real power of nodes lies in a possibility of combining them into more complex nodes, so you can:

- add nodes:
val n = n1 + n2

Result node is a node which sequentially processes input by n1 and then by n2.

- add same nodes (multiply them seqentially):

val n = n1*3

- combine nodes in parallel in two manners:

1. val n = n1 ||| n2 

It is simple parallel-input combining of n1 and n2 - input goes to each node independently and output is collected by adding outputs from subsquent nodes.

2. val n = n1 || n2

In this case an input is splitted between n1 and n2 (like when playing cards) which is suitable for some cases.

You can of course do that:

val n = n1 ||| n2 ||| n3

And that:

val n = n1 || n2 || n3 || n4

And that:

val n = n1 + n2 + (n3 ||* 2 + n4 ||| n5) * 3
.
||* and |||* are for parallel combining same nodes analogically to *.


