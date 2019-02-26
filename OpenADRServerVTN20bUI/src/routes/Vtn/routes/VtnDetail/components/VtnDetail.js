import React from 'react'
import PropTypes from 'prop-types'
import { Link } from 'react-router'
import 'react-table/react-table.css'

export const VtnDetail = ({ params, vtn, fetchVtnDetail}) => {
  var view = null;
  if(!vtn){
    fetchVtnDetail();
  }
  else {
  var supportPush = (vtn.supportPush == null) ? "" : (vtn.supportPush) ? "true":"false";
  var supportUnsecuredHttpPush = (vtn.supportUnsecuredHttpPush == null) ? "" : (vtn.supportUnsecuredHttpPush) ? "true":"false";
   view = <div className="col-md-12">
      <table className="table">
       <thead>
          <tr>
            <th>Informations</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th>VtnId</th>
            <td>{vtn.vtnId}</td>
          </tr>
          <tr>
            <th>Url</th>
            <td>https://{vtn.host}:{vtn.port}{vtn.contextPath}</td>
          </tr>
          <tr>
            <th>OadrVersion</th>
            <td>{vtn.oadrVersion}</td>
          </tr>
           <tr>
            <th>SupportPush</th>
            <td>{supportPush}</td>
          </tr>
           <tr>
            <th>SupportUnsecuredHttpPush</th>
            <td>{supportUnsecuredHttpPush}</td>
          </tr>
        
          

        </tbody>
      </table>
    </div>
  }


  return (
  <div style={{ margin: '0 auto' }} >
    <h2>Vtn</h2>
    {view}
    
    
  </div>
)}

VtnDetail.propTypes = {
  // ven: PropTypes.shape.isRequired
}


export default VtnDetail
