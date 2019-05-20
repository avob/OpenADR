"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _isEqual2 = _interopRequireDefault(require("lodash/isEqual"));

var _isPlainObject2 = _interopRequireDefault(require("lodash/isPlainObject"));

var _isFunction2 = _interopRequireDefault(require("lodash/isFunction"));

var _omit2 = _interopRequireDefault(require("lodash/omit"));

var _isString2 = _interopRequireDefault(require("lodash/isString"));

var _isNumber2 = _interopRequireDefault(require("lodash/isNumber"));

var _react = _interopRequireWildcard(require("react"));

var _propTypes = _interopRequireDefault(require("prop-types"));

var _reactSmooth = _interopRequireDefault(require("react-smooth"));

var _classnames = _interopRequireDefault(require("classnames"));

var _PureRender = _interopRequireDefault(require("../util/PureRender"));

var _Layer = _interopRequireDefault(require("../container/Layer"));

var _Trapezoid = _interopRequireDefault(require("../shape/Trapezoid"));

var _LabelList = _interopRequireDefault(require("../component/LabelList"));

var _Cell = _interopRequireDefault(require("../component/Cell"));

var _ReactUtils = require("../util/ReactUtils");

var _DataUtils = require("../util/DataUtils");

var _ChartUtils = require("../util/ChartUtils");

var _class, _class2, _temp;

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) { var desc = Object.defineProperty && Object.getOwnPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : {}; if (desc.get || desc.set) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

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

var Funnel = (0, _PureRender.default)(_class = (_temp = _class2 =
/*#__PURE__*/
function (_Component) {
  _inherits(Funnel, _Component);

  function Funnel() {
    var _getPrototypeOf2;

    var _this;

    _classCallCheck(this, Funnel);

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _possibleConstructorReturn(this, (_getPrototypeOf2 = _getPrototypeOf(Funnel)).call.apply(_getPrototypeOf2, [this].concat(args)));
    _this.state = {
      isAnimationFinished: false
    };

    _this.cachePrevData = function (trapezoids) {
      _this.setState({
        prevTrapezoids: trapezoids
      });
    };

    _this.handleAnimationEnd = function () {
      var onAnimationEnd = _this.props.onAnimationEnd;

      _this.setState({
        isAnimationFinished: true
      });

      if ((0, _isFunction2.default)(onAnimationEnd)) {
        onAnimationEnd();
      }
    };

    _this.handleAnimationStart = function () {
      var onAnimationStart = _this.props.onAnimationStart;

      _this.setState({
        isAnimationFinished: false
      });

      if ((0, _isFunction2.default)(onAnimationStart)) {
        onAnimationStart();
      }
    };

    return _this;
  }

  _createClass(Funnel, [{
    key: "componentWillReceiveProps",
    value: function componentWillReceiveProps(nextProps) {
      var _this$props = this.props,
          animationId = _this$props.animationId,
          trapezoids = _this$props.trapezoids;

      if (nextProps.isAnimationActive !== this.props.isAnimationActive) {
        this.cachePrevData([]);
      } else if (nextProps.animationId !== animationId) {
        this.cachePrevData(trapezoids);
      }
    }
  }, {
    key: "isActiveIndex",
    value: function isActiveIndex(i) {
      var activeIndex = this.props.activeIndex;

      if (Array.isArray(activeIndex)) {
        return activeIndex.indexOf(i) !== -1;
      }

      return i === activeIndex;
    }
  }, {
    key: "renderTrapezoidsStatically",
    value: function renderTrapezoidsStatically(trapezoids) {
      var _this2 = this;

      var activeShape = this.props.activeShape;
      return trapezoids.map(function (entry, i) {
        var trapezoidOptions = _this2.isActiveIndex(i) ? activeShape : null;

        var trapezoidProps = _objectSpread({}, entry, {
          stroke: entry.stroke
        });

        return _react.default.createElement(_Layer.default, _extends({
          className: "recharts-funnel-trapezoid"
        }, (0, _ReactUtils.filterEventsOfChild)(_this2.props, entry, i), {
          key: "trapezoid-".concat(i)
        }), _this2.constructor.renderTrapezoidItem(trapezoidOptions, trapezoidProps));
      });
    }
  }, {
    key: "renderTrapezoidsWithAnimation",
    value: function renderTrapezoidsWithAnimation() {
      var _this3 = this;

      var _this$props2 = this.props,
          trapezoids = _this$props2.trapezoids,
          isAnimationActive = _this$props2.isAnimationActive,
          animationBegin = _this$props2.animationBegin,
          animationDuration = _this$props2.animationDuration,
          animationEasing = _this$props2.animationEasing,
          animationId = _this$props2.animationId;
      var prevTrapezoids = this.state.prevTrapezoids;
      return _react.default.createElement(_reactSmooth.default, {
        begin: animationBegin,
        duration: animationDuration,
        isActive: isAnimationActive,
        easing: animationEasing,
        from: {
          t: 0
        },
        to: {
          t: 1
        },
        key: "funnel-".concat(animationId),
        onAnimationStart: this.handleAnimationStart,
        onAnimationEnd: this.handleAnimationEnd
      }, function (_ref) {
        var t = _ref.t;
        var stepData = trapezoids.map(function (entry, index) {
          var prev = prevTrapezoids && prevTrapezoids[index];

          if (prev) {
            var _interpolatorX = (0, _DataUtils.interpolateNumber)(prev.x, entry.x);

            var _interpolatorY = (0, _DataUtils.interpolateNumber)(prev.y, entry.y);

            var _interpolatorUpperWidth = (0, _DataUtils.interpolateNumber)(prev.upperWidth, entry.upperWidth);

            var _interpolatorLowerWidth = (0, _DataUtils.interpolateNumber)(prev.lowerWidth, entry.lowerWidth);

            var _interpolatorHeight = (0, _DataUtils.interpolateNumber)(prev.height, entry.height);

            return _objectSpread({}, entry, {
              x: _interpolatorX(t),
              y: _interpolatorY(t),
              upperWidth: _interpolatorUpperWidth(t),
              lowerWidth: _interpolatorLowerWidth(t),
              height: _interpolatorHeight(t)
            });
          }

          var interpolatorX = (0, _DataUtils.interpolateNumber)(entry.x + entry.upperWidth / 2, entry.x);
          var interpolatorY = (0, _DataUtils.interpolateNumber)(entry.y + entry.height / 2, entry.y);
          var interpolatorUpperWidth = (0, _DataUtils.interpolateNumber)(0, entry.upperWidth);
          var interpolatorLowerWidth = (0, _DataUtils.interpolateNumber)(0, entry.lowerWidth);
          var interpolatorHeight = (0, _DataUtils.interpolateNumber)(0, entry.height);
          return _objectSpread({}, entry, {
            x: interpolatorX(t),
            y: interpolatorY(t),
            upperWidth: interpolatorUpperWidth(t),
            lowerWidth: interpolatorLowerWidth(t),
            height: interpolatorHeight(t)
          });
        });
        return _react.default.createElement(_Layer.default, null, _this3.renderTrapezoidsStatically(stepData));
      });
    }
  }, {
    key: "renderTrapezoids",
    value: function renderTrapezoids() {
      var _this$props3 = this.props,
          trapezoids = _this$props3.trapezoids,
          isAnimationActive = _this$props3.isAnimationActive;
      var prevTrapezoids = this.state.prevTrapezoids;

      if (isAnimationActive && trapezoids && trapezoids.length && (!prevTrapezoids || !(0, _isEqual2.default)(prevTrapezoids, trapezoids))) {
        return this.renderTrapezoidsWithAnimation();
      }

      return this.renderTrapezoidsStatically(trapezoids);
    }
  }, {
    key: "render",
    value: function render() {
      var _this$props4 = this.props,
          hide = _this$props4.hide,
          trapezoids = _this$props4.trapezoids,
          className = _this$props4.className,
          isAnimationActive = _this$props4.isAnimationActive;
      var isAnimationFinished = this.state.isAnimationFinished;

      if (hide || !trapezoids || !trapezoids.length) {
        return null;
      }

      var layerClass = (0, _classnames.default)('recharts-trapezoids', className);
      return _react.default.createElement(_Layer.default, {
        className: layerClass
      }, this.renderTrapezoids(), (!isAnimationActive || isAnimationFinished) && _LabelList.default.renderCallByParent(this.props, trapezoids));
    }
  }], [{
    key: "renderTrapezoidItem",
    value: function renderTrapezoidItem(option, props) {
      if (_react.default.isValidElement(option)) {
        return _react.default.cloneElement(option, props);
      }

      if ((0, _isFunction2.default)(option)) {
        return option(props);
      }

      if ((0, _isPlainObject2.default)(option)) {
        return _react.default.createElement(_Trapezoid.default, _extends({}, props, option));
      }

      return _react.default.createElement(_Trapezoid.default, props);
    }
  }]);

  return Funnel;
}(_react.Component), _class2.displayName = 'Funnel', _class2.propTypes = _objectSpread({}, _ReactUtils.PRESENTATION_ATTRIBUTES, _ReactUtils.EVENT_ATTRIBUTES, {
  className: _propTypes.default.string,
  animationId: _propTypes.default.number,
  dataKey: _propTypes.default.oneOfType([_propTypes.default.string, _propTypes.default.number, _propTypes.default.func]).isRequired,
  nameKey: _propTypes.default.oneOfType([_propTypes.default.string, _propTypes.default.number, _propTypes.default.func]),
  data: _propTypes.default.arrayOf(_propTypes.default.object),
  trapezoids: _propTypes.default.arrayOf(_propTypes.default.object),
  hide: _propTypes.default.bool,
  activeShape: _propTypes.default.oneOfType([_propTypes.default.object, _propTypes.default.func, _propTypes.default.element]),
  activeIndex: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.arrayOf(_propTypes.default.number)]),
  isAnimationActive: _propTypes.default.bool,
  animationBegin: _propTypes.default.number,
  animationDuration: _propTypes.default.number,
  animationEasing: _propTypes.default.oneOf(['ease', 'ease-in', 'ease-out', 'ease-in-out', 'spring', 'linear'])
}), _class2.defaultProps = {
  stroke: '#fff',
  fill: '#808080',
  legendType: 'rect',
  labelLine: true,
  hide: false,
  isAnimationActive: !(0, _ReactUtils.isSsr)(),
  animationBegin: 400,
  animationDuration: 1500,
  animationEasing: 'ease',
  nameKey: 'name'
}, _class2.getRealFunnelData = function (item) {
  var _item$props = item.props,
      data = _item$props.data,
      children = _item$props.children;
  var presentationProps = (0, _ReactUtils.getPresentationAttributes)(item.props);
  var cells = (0, _ReactUtils.findAllByType)(children, _Cell.default);

  if (data && data.length) {
    return data.map(function (entry, index) {
      return _objectSpread({
        payload: entry
      }, presentationProps, entry, cells && cells[index] && cells[index].props);
    });
  }

  if (cells && cells.length) {
    return cells.map(function (cell) {
      return _objectSpread({}, presentationProps, cell.props);
    });
  }

  return [];
}, _class2.getRealWidthHeight = function (item, offset) {
  var customWidth = item.props.width;
  var width = offset.width,
      height = offset.height,
      left = offset.left,
      right = offset.right,
      top = offset.top,
      bottom = offset.bottom;
  var realHeight = height;
  var realWidth = width;

  if ((0, _isNumber2.default)(customWidth)) {
    realWidth = customWidth;
  } else if ((0, _isString2.default)(customWidth)) {
    realWidth = realWidth * parseFloat(customWidth) / 100;
  }

  return {
    realWidth: realWidth - left - right - 50,
    realHeight: realHeight - bottom - top,
    offsetX: (width - realWidth) / 2,
    offsetY: (height - realHeight) / 2
  };
}, _class2.getComposedData = function (_ref2) {
  var item = _ref2.item,
      offset = _ref2.offset,
      onItemMouseLeave = _ref2.onItemMouseLeave,
      onItemMouseEnter = _ref2.onItemMouseEnter;
  var funnelData = Funnel.getRealFunnelData(item);
  var _item$props2 = item.props,
      dataKey = _item$props2.dataKey,
      nameKey = _item$props2.nameKey;
  var left = offset.left,
      top = offset.top;

  var _Funnel$getRealWidthH = Funnel.getRealWidthHeight(item, offset),
      realHeight = _Funnel$getRealWidthH.realHeight,
      realWidth = _Funnel$getRealWidthH.realWidth,
      offsetX = _Funnel$getRealWidthH.offsetX,
      offsetY = _Funnel$getRealWidthH.offsetY;

  var maxValue = (0, _ChartUtils.getValueByDataKey)(funnelData[0], dataKey, 0);
  var len = funnelData.length;
  var rowHeight = realHeight / len;
  var trapezoids = funnelData.map(function (entry, i) {
    var val = (0, _ChartUtils.getValueByDataKey)(entry, dataKey, 0);
    var name = (0, _ChartUtils.getValueByDataKey)(entry, nameKey, i);
    var nextVal = 0;

    if (i !== len - 1) {
      nextVal = (0, _ChartUtils.getValueByDataKey)(funnelData[i + 1], dataKey, 0);
    }

    var x = (maxValue - val) * realWidth / (2 * maxValue) + top + 25 + offsetX;
    var y = realHeight / len * i + left + offsetY;
    var upperWidth = val / maxValue * realWidth;
    var lowerWidth = nextVal / maxValue * realWidth;
    var tooltipPayload = [{
      name: name,
      value: val,
      payload: entry,
      dataKey: dataKey
    }];
    var tooltipPosition = {
      x: x + upperWidth / 2,
      y: y + rowHeight / 2
    };
    return _objectSpread({
      x: x,
      y: y,
      width: Math.max(upperWidth, lowerWidth),
      upperWidth: upperWidth,
      lowerWidth: lowerWidth,
      height: rowHeight,
      name: name,
      val: val,
      tooltipPayload: tooltipPayload,
      tooltipPosition: tooltipPosition
    }, (0, _omit2.default)(entry, 'width'), {
      payload: entry
    });
  });
  return {
    trapezoids: trapezoids,
    data: funnelData,
    onMouseLeave: onItemMouseLeave,
    onMouseEnter: onItemMouseEnter
  };
}, _temp)) || _class;

var _default = Funnel;
exports.default = _default;