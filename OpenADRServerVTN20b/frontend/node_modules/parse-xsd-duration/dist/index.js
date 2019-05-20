(function (global, factory) {
  if (typeof define === "function" && define.amd) {
    define('parseXsdDuration', ['exports'], factory);
  } else if (typeof exports !== "undefined") {
    factory(exports);
  } else {
    var mod = {
      exports: {}
    };
    factory(mod.exports);
    global.parseXsdDuration = mod.exports;
  }
})(this, function (exports) {
  'use strict';

  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  var _emptyPeriod, _emptyTime;

  var UNITS = {
    YEAR: 31536000,
    MONTH: 2628000,
    DAY: 86400,
    HOUR: 3600,
    MINUTE: 60,
    SECOND: 1
  };

  var YEAR_UNIT = 'years';
  var MONTH_UNIT = 'months';
  var DAY_UNIT = 'days';
  var HOUR_UNIT = 'hours';
  var MINUTE_UNIT = 'minutes';
  var SECOND_UNIT = 'seconds';
  var IS_NEGATIVE_UNIT = 'isNegative';

  var emptyPeriod = (_emptyPeriod = {}, _emptyPeriod[YEAR_UNIT] = 0, _emptyPeriod[MONTH_UNIT] = 0, _emptyPeriod[DAY_UNIT] = 0, _emptyPeriod);

  var emptyTime = (_emptyTime = {}, _emptyTime[HOUR_UNIT] = 0, _emptyTime[MINUTE_UNIT] = 0, _emptyTime[SECOND_UNIT] = 0, _emptyTime);

  // Regex taken from https://www.w3.org/TR/xmlschema11-2/#duration-lexical-space
  var isValidXsdDuration = function isValidXsdDuration(str) {
    return (/^-?P((([0-9]+Y([0-9]+M)?([0-9]+D)?|([0-9]+M)([0-9]+D)?|([0-9]+D))(T(([0-9]+H)([0-9]+M)?([0-9]+(\.[0-9]+)?S)?|([0-9]+M)([0-9]+(\.[0-9]+)?S)?|([0-9]+(\.[0-9]+)?S)))?)|(T(([0-9]+H)([0-9]+M)?([0-9]+(\.[0-9]+)?S)?|([0-9]+M)([0-9]+(\.[0-9]+)?S)?|([0-9]+(\.[0-9]+)?S))))$/.test(str)
    );
  };
  var isNonEmptyString = function isNonEmptyString(input) {
    return typeof input === 'string' && input.length > 0;
  };
  var isNegative = function isNegative(str) {
    return str[0] === '-';
  };
  var stripFirstChar = function stripFirstChar(str) {
    return str.slice(1);
  };
  var unitToSeconds = function unitToSeconds(unit, amount) {
    return UNITS[unit.toUpperCase()] * amount;
  };

  var parseUnit = function parseUnit(unit, amount) {
    var amt = getNumber(amount);
    return unitToSeconds(unit, amt);
  };

  var getNumber = function getNumber(amount) {
    var amt = parseFloat(amount);
    if (isNaN(amt)) return 0;
    return amt;
  };

  var parsePeriod = function parsePeriod(period) {
    var _ref = /^(?:(\d+)Y)?(?:(\d+)M)?(?:(\d+)D)?$/g.exec(period) || [],
        year = _ref[1],
        month = _ref[2],
        day = _ref[3];

    return parseUnit('year', year) + parseUnit('month', month) + parseUnit('day', day);
  };

  var parsePeriodToObject = function parsePeriodToObject(period) {
    var _ref3;

    var _ref2 = /^(?:(\d+)Y)?(?:(\d+)M)?(?:(\d+)D)?$/g.exec(period) || [],
        years = _ref2[1],
        months = _ref2[2],
        days = _ref2[3];

    return _ref3 = {}, _ref3[YEAR_UNIT] = getNumber(years), _ref3[MONTH_UNIT] = getNumber(months), _ref3[DAY_UNIT] = getNumber(days), _ref3;
  };

  var parseTime = function parseTime(time) {
    var _ref4 = /^(?:(\d+)H)?(?:(\d+)M)?(?:(\d+(?:\.\d+)?)S)?$/g.exec(time) || [],
        hour = _ref4[1],
        minute = _ref4[2],
        second = _ref4[3];

    return parseUnit('hour', hour) + parseUnit('minute', minute) + parseUnit('second', second);
  };

  var parseTimeToObject = function parseTimeToObject(time) {
    var _ref6;

    var _ref5 = /^(?:(\d+)H)?(?:(\d+)M)?(?:(\d+(?:\.\d+)?)S)?$/g.exec(time) || [],
        hours = _ref5[1],
        minutes = _ref5[2],
        seconds = _ref5[3];

    return _ref6 = {}, _ref6[HOUR_UNIT] = getNumber(hours), _ref6[MINUTE_UNIT] = getNumber(minutes), _ref6[SECOND_UNIT] = getNumber(seconds), _ref6;
  };

  var parse = function parse(str) {
    var neg = isNegative(str);
    var duration = neg ? stripFirstChar(str) : str;
    var splitDuration = duration.split('T');
    var period = stripFirstChar(splitDuration[0]);
    var time = splitDuration[1];

    var output = 0;
    if (isNonEmptyString(period)) output += parsePeriod(period);
    if (isNonEmptyString(time)) output += parseTime(time);
    return neg ? -output : output;
  };

  var convertToObject = exports.convertToObject = function convertToObject(str) {
    var _output;

    var neg = isNegative(str);
    var duration = neg ? stripFirstChar(str) : str;
    var splitDuration = duration.split('T');
    var period = stripFirstChar(splitDuration[0]);
    var time = splitDuration[1];

    var output = (_output = {}, _output[IS_NEGATIVE_UNIT] = neg, _output);
    output = isNonEmptyString(period) ? Object.assign(output, parsePeriodToObject(period)) : Object.assign(output, emptyPeriod);
    output = isNonEmptyString(time) ? Object.assign(output, parseTimeToObject(time)) : Object.assign(output, emptyTime);
    return output;
  };

  var out = function out(input) {
    var toObject = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;

    // Throw error for non-string input
    if (typeof input !== 'string') {
      throw new TypeError('expected input to be a string');
    }
    // Return null for invalid input
    if (!isValidXsdDuration(input)) return null;
    // Parse valid input
    return toObject ? convertToObject(input) : parse(input);
  };

  exports.default = function (input, toObject) {
    return out(input, toObject);
  };
});
//# sourceMappingURL=index.js.map