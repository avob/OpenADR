import React from 'react'
import { IndexLink, Link } from 'react-router'
import PropTypes from 'prop-types'
import Breadcrumbs from 'react-breadcrumbs'
import './PageLayout.scss'

import config from 'configuration';

import reloadApp from '../../main';

class PageLayout extends React.Component {
  render() {
    var that = this;
    return  <div><div className="navbar navbar-default navbar-fixed-top">
  <div className="container">
    <div className="navbar-header">
    <Link className="navbar-brand" to='/'>{config.vtnPlatform} - AvobVTNInjector</Link>
    </div>
    <ul className="nav navbar-nav">
        <li><Link to='/vtn' activeClassName='active'>Vtn</Link></li>
        <li><Link to='/ven' activeClassName='active'>Ven</Link></li>
        <li><Link to='/event' activeClassName='active'>Event</Link></li>
    </ul>

    <ul className="nav navbar-nav navbar-right">

        {
          function(){
            if(config.user){
              return <li className="dropdown">
                <a href="#" className="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="true">
                  <i className="glyphicon glyphicon-user"/> <span className="username">{config.user}</span> <span className="caret"></span>
                </a>
                <ul className="dropdown-menu">
                  <li className="dropdown-header">Profil</li>
                  <li><a href="#" onClick={function(){
                localStorage.removeItem("login");
                localStorage.removeItem("pass");
                reloadApp();
              }}>Logout</a></li>
                </ul>
              </li>
            }
          }()
        }
  </ul>
</div>
</div>
<div className="container">
  <div className="row">
    
      {
        function(){
          if(!config.isConnectionPending){
            if(config.isConnected){
              return <div className='page-layout__viewport'>
                <Breadcrumbs
                  routes={that.props.routes}
                  params={that.props.params}
                  separator=" > " 
                />
                {that.props.children}
              </div>;
            }
            else {
              return <div className='page-layout__viewport'>
                <div className="alert alert-danger">Backend cannot be reached. Please contact your administrator</div>
              </div>
            }
          }
          else {
            return <div className='page-layout__viewport'>
                <div className="alert alert-info">Connecting to backend ...</div>
              </div>
          }
        }()
      }
          </div>
</div>
</div>
  }
}


PageLayout.propTypes = {
  children: PropTypes.node,
}

export default PageLayout
