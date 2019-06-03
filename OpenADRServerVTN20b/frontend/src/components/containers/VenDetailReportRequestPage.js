import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as venActions from '../../actions/venActions';

import { withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';


import green from '@material-ui/core/colors/green';

import VenDetailReportRequest from '../VenDetailReportRequest/VenDetailReportRequest'



function TabContainer( props ) {
  return (
  <Typography component="div" style={ { padding: 8 * 3 } }>
    { props.children }
  </Typography>
  );
}

const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  textField: {
    marginLeft: theme.spacing.unit,
    marginRight: theme.spacing.unit,
  },
  dense: {
    marginTop: 19,
  },
  menu: {
    width: 200,
  },
  formControl: {
    margin: theme.spacing.unit,
    minWidth: 500,
  },
  card: {
    maxWidth: 350,
    minWidth: 350,
  },
  media: {
    height: 40,
    paddingTop: 10,
    paddingRight: 10
  },
  button: {
    margin: theme.spacing.unit,
  },

  gridList: {
    flexWrap: 'nowrap',
    // Promote the list into his own layer on Chrome. This cost memory but helps keeping high FPS.
    transform: 'translateZ(0)',
  },
  title: {
    color: theme.palette.primary.light,
  },
  success: {
    backgroundColor: green[600],
  },
  iconButton: {
    marginTop: 10
  },

});

export class VenDetailReportRequestPage extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
  };


  componentDidMount() {
    this.props.venActions.loadVenAvailableReport( this.props.match.params.username, this.props.match.params.reportSpecifierId);
    this.props.venActions.loadVenRequestedReport(this.props.match.params.username, this.props.match.params.reportRequestId);
    this.props.venActions.loadVenRequestedReportSpecifier(this.props.match.params.username, this.props.match.params.reportRequestId);
    
  }

  render() {
    const {classes, ven_detail_report_request} = this.props;
    const {value} = this.state;

    return (
    <div className={ classes.root }>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="VEN Report" />
      </Tabs>
      <Divider variant="middle" />
      { value === 0 && <TabContainer>
      
                          <VenDetailReportRequest classes={classes}  
                            requestedReport={ven_detail_report_request.requestedReport}
                          requestedReportSpecifier={ven_detail_report_request.requestedReportSpecifier}
                            availableReport={ven_detail_report_request.availableReport}
                            />
                       </TabContainer> }

    </div>

    );
  }
}

VenDetailReportRequestPage.propTypes = {
  venActions: PropTypes.object.isRequired,


};

function mapStateToProps( state ) {
  return {
    ven_detail_report_request: state.ven_detail_report_request
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    venActions: bindActionCreators( venActions, dispatch ),
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( VenDetailReportRequestPage ) );
