import React from 'react'
import { IndexLink, Link } from 'react-router'
import PropTypes from 'prop-types'
import './VenPageLayout.scss'

export const VenPageLayout = ({ children }) => (
   <div>
  		{children}
  </div>
)

VenPageLayout.propTypes = {
  children: PropTypes.node,
}

export default VenPageLayout
