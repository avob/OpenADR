import React from 'react'
import { IndexLink, Link } from 'react-router'
import PropTypes from 'prop-types'
import './VtnPageLayout.scss'

export const VtnPageLayout = ({ children }) => (
   <div>
  		{children}
  </div>
)

VtnPageLayout.propTypes = {
  children: PropTypes.node,
}

export default VtnPageLayout
