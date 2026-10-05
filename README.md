# Hamcrest to AssertJ

![Build](https://github.com/tautastic/intellij-hamcrest-to-assertj/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/24288.svg)](https://plugins.jetbrains.com/plugin/24288)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/24288.svg)](https://plugins.jetbrains.com/plugin/24288)

<!-- Plugin description -->
Hamcrest to AssertJ refactors Hamcrest assertions to the equivalent AssertJ assertions.
It highlights Hamcrest <code>assertThat</code> calls and offers a quick fix that rewrites them, keeping the failure
message and adding the AssertJ import.

Supported matchers: <code>equalTo</code>, <code>is</code>, <code>nullValue</code>, <code>notNullValue</code>,
<code>hasSize</code>, <code>empty</code>, <code>contains</code>, <code>hasItem</code>, <code>hasItems</code>,
<code>containsString</code>, <code>containsStringIgnoringCase</code>, <code>allOf</code> and <code>anyOf</code>.
<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> -> <kbd>Plugins</kbd> -> <kbd>Marketplace</kbd> -> <kbd>Search for "Hamcrest to
  AssertJ"</kbd>->
  <kbd>Install</kbd>
  
- Manually:

  Download the [latest release](https://github.com/tautastic/intellij-hamcrest-to-assertj/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> -> <kbd>Plugins</kbd> -> <kbd>⚙️</kbd> -> <kbd>Install plugin from disk...</kbd>

## Usage

- Hover over a highlighted assertion and click on <kbd>Refactor to AssertJ</kbd>
- Right click a highlighted assertion and select <kbd>Show Context Actions</kbd> -> <kbd>Refactor to AssertJ</kbd>
- Right click a highlighted assertion and select <kbd>Show Context Actions</kbd> -> <kbd>></kbd>
  <kbd>Fix all 'Usage of Hamcrest assertions' problems in file</kbd>


## Example

```java
assertThat("wrong size", list, hasSize(2));
```

becomes

```java
Assertions.assertThat(list).as("wrong size").hasSize(2);
```

Calls that use other matchers, or a nested matcher where a plain value is expected (e.g. `hasSize(greaterThan(2))`),
are left untouched.
