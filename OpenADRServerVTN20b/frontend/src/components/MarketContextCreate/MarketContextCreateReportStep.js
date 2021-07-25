import React from 'react';







import TextField from '@material-ui/core/TextField';



import Grid from '@material-ui/core/Grid';


import Avatar from '@material-ui/core/Avatar';

import ReportTable from '../common/ReportTable'
import {Panel} from '../common/Structure'

export class MarketContextCreateReportStep extends React.Component {

  render() {
    const {classes, marketContext, hasError} = this.props;

    return (
      <React.Fragment>
        <ReportTable classes={classes} report={marketContext.reports}/>

      </React.Fragment>
 
    );
  }
}

export default MarketContextCreateReportStep;
