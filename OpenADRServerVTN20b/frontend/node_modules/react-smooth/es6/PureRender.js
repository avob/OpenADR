import _isPlainObject from "lodash/isPlainObject";
import _isEqual from "lodash/isEqual";
import _isArray from "lodash/isArray";

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


    if (_isArray(objA[keyA])) {
      if (!_isArray(objB[keyA]) || objA[keyA].length !== objB[keyA].length) {
        return false;
      } else if (!_isEqual(objA[keyA], objB[keyA])) {
        return false;
      }
    } else if (_isPlainObject(objA[keyA])) {
      if (!_isPlainObject(objB[keyA]) || !_isEqual(objA[keyA], objB[keyA])) {
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

export { shallowEqual };
export default pureRenderDecorator;