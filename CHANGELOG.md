<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Hamcrest to AssertJ Changelog

## [Unreleased]

### Added

- Support for `allOf` and `anyOf` (converted to `satisfies` and `satisfiesAnyOf`)
- Support for `is` wrapping another matcher, e.g. `is(notNullValue())`
- Support for `assertThat` calls without a failure message
- Description for the "Usage of Hamcrest assertions" inspection

### Changed

- Migrated the build to the IntelliJ Platform Gradle Plugin 2.x
- Hamcrest `contains` is now converted to `containsExactly`, as it matches the exact items in the exact order
- The plugin is now listed as "Hamcrest to AssertJ" instead of "hamcrest-to-assertj"

### Fixed

- The failure message of `assertThat("message", ...)` is no longer dropped; it is kept as `.as("message")`
- Assertions with a nested matcher in a position that expects a value, e.g. `hasSize(greaterThan(2))`, are no longer
  reported, instead of being converted to invalid code
- Removed usage of the deprecated `DynamicBundle` constructor
