import React from 'react';







import TextField from '@material-ui/core/TextField';



import Grid from '@material-ui/core/Grid';


import Avatar from '@material-ui/core/Avatar';

import DetailedReportEditTable from '../common/DetailedReportEditTable'

import {Panel} from '../common/Structure'

export class MarketContextCreateReportStep extends React.Component {


  render() {
    const {classes, marketContext, hasError, onChange, definition, filterUnit} = this.props;
    var onReportChange = (reports) => {
      marketContext.reports = reports;
      onChange(marketContext);
    }

    return (
      <React.Fragment>
        <DetailedReportEditTable classes={classes} 
        reports={marketContext.reports} 
        onReportChange={onReportChange}
        definition={definition}
        filterUnit={filterUnit}
       />

      </React.Fragment>
 
    );
  }
}

export default MarketContextCreateReportStep;
