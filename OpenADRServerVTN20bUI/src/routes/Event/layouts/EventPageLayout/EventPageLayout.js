import React from 'react'
import { IndexLink, Link } from 'react-router'
import PropTypes from 'prop-types'
import './EventPageLayout.scss'

export const EventPageLayout = ({ children }) => (
   <div>
  		{children}
  </div>
)

EventPageLayout.propTypes = {
  children: PropTypes.node,
}

export default EventPageLayout