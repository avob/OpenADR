import _isNil from "lodash/isNil";

function _objectSpread(target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i] != null ? arguments[i] : {}; var ownKeys = Object.keys(source); if (typeof Object.getOwnPropertySymbols === 'function') { ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function (sym) { return Object.getOwnPropertyDescriptor(source, sym).enumerable; })); } ownKeys.forEach(function (key) { _defineProperty(target, key, source[key]); }); } return target; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _typeof(obj) { if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; } return _typeof(obj); }

function _extends() { _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments); }

function _objectWithoutProperties(source, excluded) { if (source == null) return {}; var target = _objectWithoutPropertiesLoose(source, excluded); var key, i; if (Object.getOwnPropertySymbols) { var sourceSymbolKeys = Object.getOwnPropertySymbols(source); for (i = 0; i < sourceSymbolKeys.length; i++) { key = sourceSymbolKeys[i]; if (excluded.indexOf(key) >= 0) continue; if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue; target[key] = source[key]; } } return target; }

function _objectWithoutPropertiesLoose(source, excluded) { if (source == null) return {}; var target = {}; var sourceKeys = Object.keys(source); var key, i; for (i = 0; i < sourceKeys.length; i++) { key = sourceKeys[i]; if (excluded.indexOf(key) >= 0) continue; target[key] = source[key]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }

function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }

function _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === "object" || typeof call === "function")) { return call; } return _assertThisInitialized(self); }

function _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return self; }

function _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }

function _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }

import React, { Component } from 'react';
import PropTypes from 'prop-types';
import reduceCSSCalc from 'reduce-css-calc';
import classNames from 'classnames';
import { isNumber, isNumOrStr } from '../util/DataUtils';
import { PRESENTATION_ATTRIBUTES, getPresentationAttributes, isSsr } from '../util/ReactUtils';
import { getStringSize } from '../util/DOMUtils';
var BREAKING_SPACES = /[ \f\n\r\t\v\u2028\u2029]+/;

var calculateWordWidths = function calculateWordWidths(props) {
  try {
    var words = !_isNil(props.children) ? props.children.toString().split(BREAKING_SPACES) : [];
    var wordsWithComputedWidth = words.map(function (word) {
      return {
        word: word,
        width: getStringSize(word, props.style).width
      };
    });
    var spaceWidth = getStringSize("\xA0", props.style).width;
    return {
      wordsWithComputedWidth: wordsWithComputedWidth,
      spaceWidth: spaceWidth
    };
  } catch (e) {
    return null;
  }
};

var Text =
/*#__PURE__*/
function (_Component) {
  _inherits(Text, _Component);

  function Text() {
    var _getPrototypeOf2;

    var _this;

    _classCallCheck(this, Text);

    for (var _len = arguments.length, args = new Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    _this = _possibleConstructorReturn(this, (_getPrototypeOf2 = _getPrototypeOf(Text)).call.apply(_getPrototypeOf2, [this].concat(args)));
    _this.state = {
      wordsByLines: []
    };
    return _this;
  }

  _createClass(Text, [{
    key: "componentWillMount",
    value: function componentWillMount() {
      this.updateWordsByLines(this.props, true);
    }
  }, {
    key: "componentWillReceiveProps",
    value: function componentWillReceiveProps(nextProps) {
      var needCalculate = this.props.children !== nextProps.children || this.props.style !== nextProps.style;
      this.updateWordsByLines(nextProps, needCalculate);
    }
  }, {
    key: "updateWordsByLines",
    value: function updateWordsByLines(props, needCalculate) {
      // Only perform calculations if using features that require them (multiline, scaleToFit)
      if ((props.width || props.scaleToFit) && !isSsr()) {
        if (needCalculate) {
          var wordWidths = calculateWordWidths(props);

          if (wordWidths) {
            var wordsWithComputedWidth = wordWidths.wordsWithComputedWidth,
                spaceWidth = wordWidths.spaceWidth;
            this.wordsWithComputedWidth = wordsWithComputedWidth;
            this.spaceWidth = spaceWidth;
          } else {
            this.updateWordsWithoutCalculate(props);
            return;
          }
        }

        var wordsByLines = this.calculateWordsByLines(this.wordsWithComputedWidth, this.spaceWidth, props.width);
        this.setState({
          wordsByLines: wordsByLines
        });
      } else {
        this.updateWordsWithoutCalculate(props);
      }
    }
  }, {
    key: "updateWordsWithoutCalculate",
    value: function updateWordsWithoutCalculate(props) {
      var words = !_isNil(props.children) ? props.children.toString().split(BREAKING_SPACES) : [];
      this.setState({
        wordsByLines: [{
          words: words
        }]
      });
    }
  }, {
    key: "calculateWordsByLines",
    value: function calculateWordsByLines(wordsWithComputedWidth, spaceWidth, lineWidth) {
      var scaleToFit = this.props.scaleToFit;
      return wordsWithComputedWidth.reduce(function (result, _ref) {
        var word = _ref.word,
            width = _ref.width;
        var currentLine = result[result.length - 1];

        if (currentLine && (lineWidth == null || scaleToFit || currentLine.width + width + spaceWidth < lineWidth)) {
          // Word can be added to an existing line
          currentLine.words.push(word);
          currentLine.width += width + spaceWidth;
        } else {
          // Add first word to line or word is too long to scaleToFit on existing line
          var newLine = {
            words: [word],
            width: width
          };
          result.push(newLine);
        }

        return result;
      }, []);
    }
  }, {
    key: "render",
    value: function render() {
      var _this$props = this.props,
          dx = _this$props.dx,
          dy = _this$props.dy,
          textAnchor = _this$props.textAnchor,
          verticalAnchor = _this$props.verticalAnchor,
          scaleToFit = _this$props.scaleToFit,
          angle = _this$props.angle,
          lineHeight = _this$props.lineHeight,
          capHeight = _this$props.capHeight,
          className = _this$props.className,
          textProps = _objectWithoutProperties(_this$props, ["dx", "dy", "textAnchor", "verticalAnchor", "scaleToFit", "angle", "lineHeight", "capHeight", "className"]);

      var wordsByLines = this.state.wordsByLines;

      if (!isNumOrStr(textProps.x) || !isNumOrStr(textProps.y)) {
        return null;
      }

      var x = textProps.x + (isNumber(dx) ? dx : 0);
      var y = textProps.y + (isNumber(dy) ? dy : 0);
      var startDy;

      switch (verticalAnchor) {
        case 'start':
          startDy = reduceCSSCalc("calc(".concat(capHeight, ")"));
          break;

        case 'middle':
          startDy = reduceCSSCalc("calc(".concat((wordsByLines.length - 1) / 2, " * -").concat(lineHeight, " + (").concat(capHeight, " / 2))"));
          break;

        default:
          startDy = reduceCSSCalc("calc(".concat(wordsByLines.length - 1, " * -").concat(lineHeight, ")"));
          break;
      }

      var transforms = [];

      if (scaleToFit) {
        var lineWidth = wordsByLines[0].width;
        transforms.push("scale(".concat(this.props.width / lineWidth, ")"));
      }

      if (angle) {
        transforms.push("rotate(".concat(angle, ", ").concat(x, ", ").concat(y, ")"));
      }

      if (transforms.length) {
        textProps.transform = transforms.join(' ');
      }

      return React.createElement("text", _extends({}, getPresentationAttributes(textProps), {
        x: x,
        y: y,
        className: classNames('recharts-text', className),
        textAnchor: textAnchor
      }), wordsByLines.map(function (line, index) {
        return React.createElement("tspan", {
          x: x,
          dy: index === 0 ? startDy : lineHeight,
          key: index
        }, line.words.join(' '));
      }));
    }
  }]);

  return Text;
}(Component);

Text.propTypes = _objectSpread({}, PRESENTATION_ATTRIBUTES, {
  scaleToFit: PropTypes.bool,
  angle: PropTypes.number,
  textAnchor: PropTypes.oneOf(['start', 'middle', 'end', 'inherit']),
  verticalAnchor: PropTypes.oneOf(['start', 'middle', 'end']),
  style: PropTypes.object
});
Text.defaultProps = {
  x: 0,
  y: 0,
  lineHeight: '1em',
  capHeight: '0.71em',
  // Magic number from d3
  scaleToFit: false,
  textAnchor: 'start',
  verticalAnchor: 'end' // Maintain compat with existing charts / default SVG behavior

};
export default Text;