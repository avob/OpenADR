import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';
import * as venActions from '../../actions/venActions';


import { withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';

import VenDetailSettings from '../VenDetail/VenDetailSettings'
import VenDetailReport from '../VenDetail/VenDetailReport'
import VenDetailRequest from '../VenDetail/VenDetailRequest'
import VenDetailOptSchedule from '../VenDetail/VenDetailOptSchedule'
import VenDetailEnrollment from '../VenDetail/VenDetailEnrollment'
import VenDetailGroup from '../VenDetail/VenDetailGroup'

import VenDetailReportDescription from '../VenDetail/VenDetailReportDescription'
import VenDetailRequestSpecifier from '../VenDetail/VenDetailRequestSpecifier'





import { history } from '../../store/configureStore';
import Paper from '@material-ui/core/Paper';
import Toolbar from '@material-ui/core/Toolbar';
import Button from '@material-ui/core/Button';
import { VenActionDialog } from '../common/VtnconfigurationDialog'




const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  textField: {
    marginLeft: 0,
    marginRight: 0,
  },
  dense: {
    marginTop: 19,
  },
  menu: {
    width: 200,
  },
  formControl: {
    margin: theme.spacing(1),
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
    margin: theme.spacing(1),
  },
  iconButton: {
    marginTop: 10
  },
});

export class VenDetailPage extends React.Component {
  
  constructor() {
    super();
    this.state = {
      value: 0,
    };

    
  }

  handleDialogOpen = () => {
    this.setState({dialogOpen: true})
  }

  handleDialogClose = () => {
    this.setState({dialogOpen: false})
  }

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
    switch(value) {

      default:
        break;
    }
  };


  componentDidMount() {
	  

    switch(this.props.match.params.panel){
      
      default:
        this.setState({value:0});
        break;
    }
  }


  render() {
    const {classes, ven_detail} = this.props;
    const {value} = this.state;
    return (
    <div className={ classes.root }>
    <Paper>
    <Toolbar>
            <div style={{flex: '0 0 auto'}}>
              <Typography variant="h6" id="tableTitle">
                  {ven_detail.ven.commonName}
                </Typography>
            </div>
            <div style={{flex: '1 1 100%'}} />
            <div style={{ flex: '0 0 auto'}}>

            </div>
                


        </Toolbar>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="Settings" />
        <Tab label="Reports" />
    	<Tab label="Requests" />
    	<Tab label="OptSchedules" />
        <Tab label="Enrollments" />
        <Tab label="Groups" />
      </Tabs>
      <Divider style={{margin: "20px 0"}}/>


                                             
      </Paper>

    </div>

    );
  }
}

VenDetailPage.propTypes = {
  venActions: PropTypes.object.isRequired,
  vtnConfigurationActions: PropTypes.object.isRequired,


};

function mapStateToProps( state ) {
  return {
    ven_detail: state.ven_detail
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    venActions: bindActionCreators( venActions, dispatch ),
    vtnConfigurationActions: bindActionCreators( vtnConfigurationActions, dispatch ),
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( VenDetailPage ) );
