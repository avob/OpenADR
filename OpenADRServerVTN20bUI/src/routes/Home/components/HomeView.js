import React from 'react'
import './HomeView.scss'

import { IndexLink, Link } from 'react-router'


export const HomeView = () => (
  <div style={{ margin: '0 auto' }}>
  	<h2>Home</h2>
  	<div className="panel panel-default">
	  <div className="panel-body">
	    <Link to={{
          pathname: '/ven/'
        }}>List Connected Virtual End Nodes (VEN)</Link>
	  </div>
	   <div className="panel-body">
	    <Link to={{
          pathname: '/avobvenservice/'
        }}>List configured VEN service requests</Link>
	  </div>
	  <div className="panel-body">
	    <Link to={{
          pathname: '/event/'
        }}>List configured Demand Response Events</Link>
	  </div>
	</div>
  </div>
)

export default HomeView
