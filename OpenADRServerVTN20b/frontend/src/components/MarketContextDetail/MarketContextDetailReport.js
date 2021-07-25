import React from 'react';


import Grid from '@material-ui/core/Grid';

import ReportTable from '../common/ReportTable'
import {Panel} from '../common/Structure'



export class MarketContextDetailReport extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}


  }

  render() {
    const {classes, marketContext} = this.props;


    return (
    <div className={ classes.root } >
      <ReportTable classes={classes} report={marketContext.reports}/>



     
    </div>
    );
  }
}

export default MarketContextDetailReport;
