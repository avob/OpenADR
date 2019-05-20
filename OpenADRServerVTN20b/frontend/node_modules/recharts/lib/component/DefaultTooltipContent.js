"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _isArray2 = _interopRequireDefault(require("lodash/isArray"));

var _react = _interopRequireWildcard(require("react"));

var _propTypes = _interopRequireDefault(require("prop-types"));

var _classnames = _interopRequireDefault(require("classnames"));

var _PureRender = _interopRequireDefault(require("../util/PureRender"));

var _DataUtils = require("../util/DataUtils");

var _class, _class2, _temp;

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) { var desc = Object.defineProperty && Object.getOwnPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : {}; if (desc.get || desc.set) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _slicedToArray(arr, i) { return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _nonIterableRest(); }

function _nonIterableRest() { throw new TypeError("Invalid attempt to destructure non-iterable instance"); }

function _iterableToArrayLimit(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"] != null) _i["return"](); } finally { if (_d) throw _e; } } return _arr; }

function _arrayWithHoles(arr) { if (Array.isArray(arr)) return arr; }

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

var defaultFormatter = function defaultFormatter(value) {
  return (0, _isArray2.default)(value) && (0, _DataUtils.isNumOrStr)(value[0]) && (0, _DataUtils.isNumOrStr)(value[1]) ? value.join(' ~ ') : value;
};

var DefaultTooltipContent = (0, _PureRender.default)(_class = (_temp = _class2 =
/*#__PURE__*/
function (_Component) {
  _inherits(DefaultTooltipContent, _Component);

  function DefaultTooltipContent() {
    _classCallCheck(this, DefaultTooltipContent);

    return _possibleConstructorReturn(this, _getPrototypeOf(DefaultTooltipContent).apply(this, arguments));
  }

  _createClass(DefaultTooltipContent, [{
    key: "renderContent",
    value: function renderContent() {
      var _this$props = this.props,
          payload = _this$props.payload,
          separator = _this$props.separator,
          formatter = _this$props.formatter,
          itemStyle = _this$props.itemStyle,
          itemSorter = _this$props.itemSorter;

      if (payload && payload.length) {
        var listStyle = {
          padding: 0,
          margin: 0
        };
        var items = payload.sort(itemSorter).map(function (entry, i) {
          var finalItemStyle = _objectSpread({
            display: 'block',
            paddingTop: 4,
            paddingBottom: 4,
            color: entry.color || '#000'
          }, itemStyle);

          var finalFormatter = entry.formatter || formatter || defaultFormatter;
          var name = entry.name,
              value = entry.value;

          if (finalFormatter) {
            var formatted = finalFormatter(value, name, entry, i);

            if (Array.isArray(formatted)) {
              var _formatted = _slicedToArray(formatted, 2);

              value = _formatted[0];
              name = _formatted[1];
            } else {
              value = formatted;
            }
          }

          return _react.default.createElement("li", {
            className: "recharts-tooltip-item",
            key: "tooltip-item-".concat(i),
            style: finalItemStyle
          }, (0, _DataUtils.isNumOrStr)(name) ? _react.default.createElement("span", {
            className: "recharts-tooltip-item-name"
          }, name) : null, (0, _DataUtils.isNumOrStr)(name) ? _react.default.createElement("span", {
            className: "recharts-tooltip-item-separator"
          }, separator) : null, _react.default.createElement("span", {
            className: "recharts-tooltip-item-value"
          }, value), _react.default.createElement("span", {
            className: "recharts-tooltip-item-unit"
          }, entry.unit || ''));
        });
        return _react.default.createElement("ul", {
          className: "recharts-tooltip-item-list",
          style: listStyle
        }, items);
      }

      return null;
    }
  }, {
    key: "render",
    value: function render() {
      var _this$props2 = this.props,
          wrapperClassName = _this$props2.wrapperClassName,
          contentStyle = _this$props2.contentStyle,
          labelClassName = _this$props2.labelClassName,
          labelStyle = _this$props2.labelStyle,
          label = _this$props2.label,
          labelFormatter = _this$props2.labelFormatter;

      var finalStyle = _objectSpread({
        margin: 0,
        padding: 10,
        backgroundColor: '#fff',
        border: '1px solid #ccc',
        whiteSpace: 'nowrap'
      }, contentStyle);

      var finalLabelStyle = _objectSpread({
        margin: 0
      }, labelStyle);

      var hasLabel = (0, _DataUtils.isNumOrStr)(label);
      var finalLabel = hasLabel ? label : '';
      var wrapperCN = (0, _classnames.default)('recharts-default-tooltip', wrapperClassName);
      var labelCN = (0, _classnames.default)('recharts-tooltip-label', labelClassName);

      if (hasLabel && labelFormatter) {
        finalLabel = labelFormatter(label);
      }

      return _react.default.createElement("div", {
        className: wrapperCN,
        style: finalStyle
      }, _react.default.createElement("p", {
        className: labelCN,
        style: finalLabelStyle
      }, finalLabel), this.renderContent());
    }
  }]);

  return DefaultTooltipContent;
}(_react.Component), _class2.displayName = 'DefaultTooltipContent', _class2.propTypes = {
  separator: _propTypes.default.string,
  wrapperClassName: _propTypes.default.string,
  labelClassName: _propTypes.default.string,
  formatter: _propTypes.default.func,
  contentStyle: _propTypes.default.object,
  itemStyle: _propTypes.default.object,
  labelStyle: _propTypes.default.object,
  labelFormatter: _propTypes.default.func,
  label: _propTypes.default.any,
  payload: _propTypes.default.arrayOf(_propTypes.default.shape({
    name: _propTypes.default.any,
    value: _propTypes.default.oneOfType([_propTypes.default.number, _propTypes.default.string, _propTypes.default.array]),
    unit: _propTypes.default.any
  })),
  itemSorter: _propTypes.default.func
}, _class2.defaultProps = {
  separator: ' : ',
  contentStyle: {},
  itemStyle: {},
  labelStyle: {}
}, _temp)) || _class;

var _default = DefaultTooltipContent;
exports.default = _default;