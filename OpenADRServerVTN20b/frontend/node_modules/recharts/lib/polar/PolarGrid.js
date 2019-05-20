"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _react = _interopRequireWildcard(require("react"));

var _propTypes = _interopRequireDefault(require("prop-types"));

var _PureRender = _interopRequireDefault(require("../util/PureRender"));

var _PolarUtils = require("../util/PolarUtils");

var _ReactUtils = require("../util/ReactUtils");

var _class, _class2, _temp;

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) { var desc = Object.defineProperty && Object.getOwnPropertyDescriptor ? Object.getOwnPropertyDescriptor(obj, key) : {}; if (desc.get || desc.set) { Object.defineProperty(newObj, key, desc); } else { newObj[key] = obj[key]; } } } } newObj.default = obj; return newObj; } }

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

var PolarGrid = (0, _PureRender.default)(_class = (_temp = _class2 =
/*#__PURE__*/
function (_Component) {
  _inherits(PolarGrid, _Component);

  function PolarGrid() {
    _classCallCheck(this, PolarGrid);

    return _possibleConstructorReturn(this, _getPrototypeOf(PolarGrid).apply(this, arguments));
  }

  _createClass(PolarGrid, [{
    key: "getPolygonPath",
    value: function getPolygonPath(radius) {
      var _this$props = this.props,
          cx = _this$props.cx,
          cy = _this$props.cy,
          polarAngles = _this$props.polarAngles;
      var path = '';
      polarAngles.forEach(function (angle, i) {
        var point = (0, _PolarUtils.polarToCartesian)(cx, cy, radius, angle);

        if (i) {
          path += "L ".concat(point.x, ",").concat(point.y);
        } else {
          path += "M ".concat(point.x, ",").concat(point.y);
        }
      });
      path += 'Z';
      return path;
    }
    /**
     * Draw axis of radial line
     * @return {[type]} The lines
     */

  }, {
    key: "renderPolarAngles",
    value: function renderPolarAngles() {
      var _this$props2 = this.props,
          cx = _this$props2.cx,
          cy = _this$props2.cy,
          innerRadius = _this$props2.innerRadius,
          outerRadius = _this$props2.outerRadius,
          polarAngles = _this$props2.polarAngles;

      if (!polarAngles || !polarAngles.length) {
        return null;
      }

      var props = _objectSpread({
        stroke: '#ccc'
      }, (0, _ReactUtils.getPresentationAttributes)(this.props));

      return _react.default.createElement("g", {
        className: "recharts-polar-grid-angle"
      }, polarAngles.map(function (entry, i) {
        var start = (0, _PolarUtils.polarToCartesian)(cx, cy, innerRadius, entry);
        var end = (0, _PolarUtils.polarToCartesian)(cx, cy, outerRadius, entry);
        return _react.default.createElement("line", _extends({}, props, {
          key: "line-".concat(i),
          x1: start.x,
          y1: start.y,
          x2: end.x,
          y2: end.y
        }));
      }));
    }
    /**
     * Draw concentric circles
     * @param {Number} radius The radius of circle
     * @param {Number} index  The index of circle
     * @param {Object} extraProps Extra props
     * @return {ReactElement} circle
     */

  }, {
    key: "renderConcentricCircle",
    value: function renderConcentricCircle(radius, index, extraProps) {
      var _this$props3 = this.props,
          cx = _this$props3.cx,
          cy = _this$props3.cy;

      var props = _objectSpread({
        stroke: '#ccc'
      }, (0, _ReactUtils.getPresentationAttributes)(this.props), {
        fill: 'none'
      }, extraProps);

      return _react.default.createElement("circle", _extends({}, props, {
        className: "recharts-polar-grid-concentric-circle",
        key: "circle-".concat(index),
        cx: cx,
        cy: cy,
        r: radius
      }));
    }
    /**
     * Draw concentric polygons
     * @param {Number} radius     The radius of polygon
     * @param {Number} index      The index of polygon
     * @param {Object} extraProps Extra props
     * @return {ReactElement} polygon
     */

  }, {
    key: "renderConcentricPolygon",
    value: function renderConcentricPolygon(radius, index, extraProps) {
      var props = _objectSpread({
        stroke: '#ccc'
      }, (0, _ReactUtils.getPresentationAttributes)(this.props), {
        fill: 'none'
      }, extraProps);

      return _react.default.createElement("path", _extends({}, props, {
        className: "recharts-polar-grid-concentric-polygon",
        key: "path-".concat(index),
        d: this.getPolygonPath(radius)
      }));
    }
    /**
     * Draw concentric axis
     * @return {ReactElement} Concentric axis
     * @todo Optimize the name
     */

  }, {
    key: "renderConcentricPath",
    value: function renderConcentricPath() {
      var _this = this;

      var _this$props4 = this.props,
          polarRadius = _this$props4.polarRadius,
          gridType = _this$props4.gridType;

      if (!polarRadius || !polarRadius.length) {
        return null;
      }

      return _react.default.createElement("g", {
        className: "recharts-polar-grid-concentric"
      }, polarRadius.map(function (entry, i) {
        return gridType === 'circle' ? _this.renderConcentricCircle(entry, i) : _this.renderConcentricPolygon(entry, i);
      }));
    }
  }, {
    key: "render",
    value: function render() {
      var outerRadius = this.props.outerRadius;

      if (outerRadius <= 0) {
        return null;
      }

      return _react.default.createElement("g", {
        className: "recharts-polar-grid"
      }, this.renderPolarAngles(), this.renderConcentricPath());
    }
  }]);

  return PolarGrid;
}(_react.Component), _class2.displayName = 'PolarGrid', _class2.propTypes = _objectSpread({}, _ReactUtils.PRESENTATION_ATTRIBUTES, {
  cx: _propTypes.default.number,
  cy: _propTypes.default.number,
  innerRadius: _propTypes.default.number,
  outerRadius: _propTypes.default.number,
  polarAngles: _propTypes.default.arrayOf(_propTypes.default.number),
  polarRadius: _propTypes.default.arrayOf(_propTypes.default.number),
  gridType: _propTypes.default.oneOf(['polygon', 'circle'])
}), _class2.defaultProps = {
  cx: 0,
  cy: 0,
  innerRadius: 0,
  outerRadius: 0,
  gridType: 'polygon'
}, _temp)) || _class;

var _default = PolarGrid;
exports.default = _default;