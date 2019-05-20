import _isNil from "lodash/isNil";
import _isFunction from "lodash/isFunction";
import _uniqBy from "lodash/uniqBy";

var _class, _class2, _temp;

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i] != null ? arguments[i] : {}; var ownKeys = Object.keys(source); if (typeof Object.getOwnPropertySymbols === 'function') { ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function (sym) { return Object.getOwnPropertyDescriptor(source, sym).enumerable; })); } ownKeys.forEach(function (key) { _defineProperty(target, key, source[key]); }); } return target; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

/**
 * @fileOverview Tooltip
 */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { translateStyle } from 'react-smooth';
import classNames from 'classnames';
import DefaultTooltipContent from './DefaultTooltipContent';
import { isSsr } from '../util/ReactUtils';
import { isNumber } from '../util/DataUtils';
import pureRender from '../util/PureRender';
var CLS_PREFIX = 'recharts-tooltip-wrapper';
var EPS = 1;

var defaultUniqBy = function defaultUniqBy(entry) {
  return entry.dataKey;
};

var getUniqPaylod = function getUniqPaylod(option, payload) {
  if (option === true) {
    return _uniqBy(payload, defaultUniqBy);
  }

  if (_isFunction(option)) {
    return _uniqBy(payload, option);
  }

  return payload;
};

var propTypes = {
  content: PropTypes.oneOfType([PropTypes.element, PropTypes.func]),
  viewBox: PropTypes.shape({
    x: PropTypes.number,
    y: PropTypes.number,
    width: PropTypes.number,
    height: PropTypes.number
  }),
  active: PropTypes.bool,
  separator: PropTypes.string,
  formatter: PropTypes.func,
  offset: PropTypes.number,
  itemStyle: PropTypes.object,
  labelStyle: PropTypes.object,
  wrapperStyle: PropTypes.object,
  contentStyle: PropTypes.object,
  cursor: PropTypes.oneOfType([PropTypes.bool, PropTypes.element, PropTypes.object]),
  coordinate: PropTypes.shape({
    x: PropTypes.number,
    y: PropTypes.number
  }),
  position: PropTypes.shape({
    x: PropTypes.number,
    y: PropTypes.number
  }),
  label: PropTypes.any,
  payload: PropTypes.arrayOf(PropTypes.shape({
    name: PropTypes.any,
    value: PropTypes.oneOfType([PropTypes.number, PropTypes.string, PropTypes.array]),
    unit: PropTypes.any
  })),
  paylodUniqBy: PropTypes.oneOfType([PropTypes.func, PropTypes.bool]),
  isAnimationActive: PropTypes.bool,
  animationDuration: PropTypes.number,
  animationEasing: PropTypes.oneOf(['ease', 'ease-in', 'ease-out', 'ease-in-out', 'linear']),
  itemSorter: PropTypes.func,
  filterNull: PropTypes.bool,
  useTranslate3d: PropTypes.bool
};
var defaultProps = {
  active: false,
  offset: 10,
  viewBox: {
    x1: 0,
    x2: 0,
    y1: 0,
    y2: 0
  },
  coordinate: {
    x: 0,
    y: 0
  },
  cursorStyle: {},
  separator: ' : ',
  wrapperStyle: {},
  contentStyle: {},
  itemStyle: {},
  labelStyle: {},
  cursor: true,
  isAnimationActive: !isSsr(),
  animationEasing: 'ease',
  animationDuration: 400,
  itemSorter: function itemSorter() {
    return -1;
  },
  filterNull: true,
  useTranslate3d: false
};

var renderContent = function renderContent(content, props) {
  if (React.isValidElement(content)) {
    return React.cloneElement(content, props);
  }

  if (_isFunction(content)) {
    return content(props);
  }

  return React.createElement(DefaultTooltipContent, props);
};

var Tooltip = pureRender(_class = (_temp = _class2 =
/*#__PURE__*/
function (_Component) {
  _inherits(Tooltip, _Component);

  function Tooltip() {
    var _getPrototypeOf2;

    var _this;

    _classCallCheck(this, Tooltip);

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _possibleConstructorReturn(this, (_getPrototypeOf2 = _getPrototypeOf(Tooltip)).call.apply(_getPrototypeOf2, [this].concat(args)));
    _this.state = {
      boxWidth: -1,
      boxHeight: -1
    };
    return _this;
  }

  _createClass(Tooltip, [{
    key: "componentDidMount",
    value: function componentDidMount() {
      this.updateBBox();
    }
  }, {
    key: "componentDidUpdate",
    value: function componentDidUpdate() {
      this.updateBBox();
    }
  }, {
    key: "updateBBox",
    value: function updateBBox() {
      var _this$state = this.state,
          boxWidth = _this$state.boxWidth,
          boxHeight = _this$state.boxHeight;

      if (this.wrapperNode && this.wrapperNode.getBoundingClientRect) {
        var box = this.wrapperNode.getBoundingClientRect();

        if (Math.abs(box.width - boxWidth) > EPS || Math.abs(box.height - boxHeight) > EPS) {
          this.setState({
            boxWidth: box.width,
            boxHeight: box.height
          });
        }
      } else if (boxWidth !== -1 || boxHeight !== -1) {
        this.setState({
          boxWidth: -1,
          boxHeight: -1
        });
      }
    }
  }, {
    key: "render",
    value: function render() {
      var _classNames,
          _this2 = this;

      var _this$props = this.props,
          payload = _this$props.payload,
          isAnimationActive = _this$props.isAnimationActive,
          animationDuration = _this$props.animationDuration,
          animationEasing = _this$props.animationEasing,
          filterNull = _this$props.filterNull,
          paylodUniqBy = _this$props.paylodUniqBy;
      var finalPayload = getUniqPaylod(paylodUniqBy, filterNull && payload && payload.length ? payload.filter(function (entry) {
        return !_isNil(entry.value);
      }) : payload);
      var hasPayload = finalPayload && finalPayload.length;
      var _this$props2 = this.props,
          content = _this$props2.content,
          viewBox = _this$props2.viewBox,
          coordinate = _this$props2.coordinate,
          position = _this$props2.position,
          active = _this$props2.active,
          offset = _this$props2.offset,
          wrapperStyle = _this$props2.wrapperStyle;

      var outerStyle = _objectSpread({
        pointerEvents: 'none',
        visibility: active && hasPayload ? 'visible' : 'hidden',
        position: 'absolute',
        top: 0
      }, wrapperStyle);

      var translateX, translateY;

      if (position && isNumber(position.x) && isNumber(position.y)) {
        translateX = position.x;
        translateY = position.y;
      } else {
        var _this$state2 = this.state,
            boxWidth = _this$state2.boxWidth,
            boxHeight = _this$state2.boxHeight;

        if (boxWidth > 0 && boxHeight > 0 && coordinate) {
          translateX = position && isNumber(position.x) ? position.x : Math.max(coordinate.x + boxWidth + offset > viewBox.x + viewBox.width ? coordinate.x - boxWidth - offset : coordinate.x + offset, viewBox.x);
          translateY = position && isNumber(position.y) ? position.y : Math.max(coordinate.y + boxHeight + offset > viewBox.y + viewBox.height ? coordinate.y - boxHeight - offset : coordinate.y + offset, viewBox.y);
        } else {
          outerStyle.visibility = 'hidden';
        }
      }

      outerStyle = _objectSpread({}, outerStyle, translateStyle({
        transform: this.props.useTranslate3d ? "translate3d(".concat(translateX, "px, ").concat(translateY, "px, 0)") : "translate(".concat(translateX, "px, ").concat(translateY, "px)")
      }));

      if (isAnimationActive && active) {
        outerStyle = _objectSpread({}, outerStyle, translateStyle({
          transition: "transform ".concat(animationDuration, "ms ").concat(animationEasing)
        }));
      }

      var cls = classNames(CLS_PREFIX, (_classNames = {}, _defineProperty(_classNames, "".concat(CLS_PREFIX, "-right"), isNumber(translateX) && coordinate && isNumber(coordinate.x) && translateX >= coordinate.x), _defineProperty(_classNames, "".concat(CLS_PREFIX, "-left"), isNumber(translateX) && coordinate && isNumber(coordinate.x) && translateX < coordinate.x), _defineProperty(_classNames, "".concat(CLS_PREFIX, "-bottom"), isNumber(translateY) && coordinate && isNumber(coordinate.y) && translateY >= coordinate.y), _defineProperty(_classNames, "".concat(CLS_PREFIX, "-top"), isNumber(translateY) && coordinate && isNumber(coordinate.y) && translateY < coordinate.y), _classNames));
      return React.createElement("div", {
        className: cls,
        style: outerStyle,
        ref: function ref(node) {
          _this2.wrapperNode = node;
        }
      }, renderContent(content, _objectSpread({}, this.props, {
        payload: finalPayload
      })));
    }
  }]);

  return Tooltip;
}(Component), _class2.displayName = 'Tooltip', _class2.propTypes = propTypes, _class2.defaultProps = defaultProps, _temp)) || _class;

export default Tooltip;