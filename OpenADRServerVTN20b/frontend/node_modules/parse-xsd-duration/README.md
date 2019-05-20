# parse-xsd-duration [![Version](https://img.shields.io/npm/v/parse-xsd-duration.svg)][npm] [![Build Status](https://travis-ci.com/mickdekkers/parse-xsd-duration.svg?branch=master)](https://travis-ci.com/mickdekkers/parse-xsd-duration)
Parse XSD durations to seconds (by default) or to object

## Installation

```bash
$ npm install parse-xsd-duration
```

This package has zero dependencies and is provided in [UMD format][umd] so you can easily use it in the browser.

## Usage

```js
import pxd from 'parse-xsd-duration'

pxd('PT2M10S')
// => 130

pxd('P2Y6M5DT12H35M30S')
// => 79317330

pxd('P1Y2M3DT5H20M30.123S')
// => 37070430.123

pxd('PT2M10S', true)
// => { years: 0, months: 0, days: 0, hours: 0, minutes: 2, seconds: 10, isNegative: 0 }

pxd('P2Y6M5DT12H35M30S', true)
// => { years: 2, months: 6, days: 5, hours: 12, minutes: 35, seconds: 30, isNegative: 0 }

pxd('P1Y2M3DT5H20M30.123S', true)
// => { years: 1, months: 2, days: 3, hours: 5, minutes: 20, seconds: 30.123, isNegative: 0 }
```

## API

### `parseXsdDuration(xsdDuration: string, toObject: boolean): number | object | null`

- Converts an XSD duration string to seconds by default or to object with parameter `toObject` = `true`.
- If the string is not a valid XSD duration, it will return `null`.
- If the input is not a string, it will throw a `TypeError`.

## License

MIT © [Mick Dekkers][mickdekkers-gh]

[npm]: https://www.npmjs.com/package/parse-xsd-duration
[umd]: https://github.com/umdjs/umd
[mickdekkers-gh]: https://github.com/mickdekkers
