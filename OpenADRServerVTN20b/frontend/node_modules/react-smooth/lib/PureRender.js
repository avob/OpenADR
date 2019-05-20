"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.shallowEqual = shallowEqual;
exports.default = void 0;

var _isPlainObject2 = _interopRequireDefault(require("lodash/isPlainObject"));

var _isEqual2 = _interopRequireDefault(require("lodash/isEqual"));

var _isArray2 = _interopRequireDefault(require("lodash/isArray"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function shallowEqual(objA, objB) {
  if (objA === objB) {
    return true;
  }

  if (_typeof(objA) !== 'object' || objA === null || _typeof(objB) !== 'object' || objB === null) {
    return false;
  }

  var keysA = Object.keys(objA);
  var keysB = Object.keys(objB);

  if (keysA.length !== keysB.length) {
    return false;
  }

  var bHasOwnProperty = hasOwnProperty.bind(objB);

  for (var i = 0; i < keysA.length; i++) {
    var keyA = keysA[i];

    if (objA[keyA] === objB[keyA]) {
      continue;
    } // special diff with Array or Object


    if ((0, _isArray2.default)(objA[keyA])) {
      if (!(0, _isArray2.default)(objB[keyA]) || objA[keyA].length !== objB[keyA].length) {
        return false;
      } else if (!(0, _isEqual2.default)(objA[keyA], objB[keyA])) {
        return false;
      }
    } else if ((0, _isPlainObject2.default)(objA[keyA])) {
      if (!(0, _isPlainObject2.default)(objB[keyA]) || !(0, _isEqual2.default)(objA[keyA], objB[keyA])) {
        return false;
      }
    } else if (!bHasOwnProperty(keysA[i]) || objA[keysA[i]] !== objB[keysA[i]]) {
      return false;
    }
  }

  return true;
}

function shallowCompare(instance, nextProps, nextState) {
  return !shallowEqual(instance.props, nextProps) || !shallowEqual(instance.state, nextState);
}

function shouldComponentUpdate(nextProps, nextState) {
  return shallowCompare(this, nextProps, nextState);
}
/* eslint-disable no-param-reassign */


function pureRenderDecorator(component) {
  component.prototype.shouldComponentUpdate = shouldComponentUpdate;
}

var _default = pureRenderDecorator;
exports.default = _default;