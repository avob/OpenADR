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

import green from '@material-ui/core/colors/green';

import { history } from '../../store/configureStore';




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
  }

});

export class VenDetailPage extends React.Component {
  
  constructor() {
    super();
    this.state = {
      value: 0,
    };
    
  }

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
    switch(value) {
      case 0:
        history.push("/ven/detail/"+this.props.match.params.username+"/settings")
        break;
      case 1:
        history.push("/ven/detail/"+this.props.match.params.username+"/reports")
        break;
      case 2:
        history.push("/ven/detail/"+this.props.match.params.username+"/optschedules")
        break;
    }
    
  };


  componentDidMount() {
    this.props.vtnConfigurationActions.loadMarketContext();
    this.props.vtnConfigurationActions.loadGroup();
    this.props.venActions.loadVenDetail( this.props.match.params.username );
    this.props.venActions.loadVenGroup( this.props.match.params.username );
    this.props.venActions.loadVenMarketContext( this.props.match.params.username );
    this.props.venActions.loadVenAvailableReport( this.props.match.params.username );
    this.props.venActions.loadVenRequestedReport( this.props.match.params.username );
    switch(this.props.match.params.panel){
      case "settings":
        this.setState({value:0});
        break;
      case "reports":
        this.setState({value:1});
        break;
      case "optschedules":
        this.setState({value:2});
        break;
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
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="VEN Settings" />
        <Tab label="Reports" />
        <Tab label="OptSchedules" />
      </Tabs>
      <Divider variant="middle" />
      { value === 0 && <TabContainer>
                         <VenDetailSettings classes={ classes }
                                            ven={ ven_detail.ven }
                                            marketContext={ ven_detail.marketContext }
                                            group={ ven_detail.group }
                                            venMarketContext={ ven_detail.venMarketContext }
                                            venGroup={ ven_detail.venGroup }
                                            addVenMarketContext={ this.props.venActions.addVenMarketContext }
                                            removeVenMarketContext={ this.props.venActions.removeVenMarketContext }
                                            addVenGroup={ this.props.venActions.addVenGroup }
                                            removeVenGroup={ this.props.venActions.removeVenGroup }
                                            registerPartyRequestReregistration={this.props.venActions.registerPartyRequestReregistration}
                                            registerPartyCancelPartyRegistration={this.props.venActions.registerPartyCancelPartyRegistration}
                                            cleanRegistration={this.props.venActions.cleanRegistration}
                                             />
                                            
                                            
                       </TabContainer> }
      { value === 1 && <TabContainer>
                        <VenDetailReport classes={ classes }
                                            ven={ ven_detail.ven }
                                            marketContext={ ven_detail.marketContext }
                                            availableReport={ven_detail.availableReport}
                                            requestedReport={ven_detail.requestedReport}
                                            requestRegisterReport={this.props.venActions.requestRegisterReport}
                                            sendRegisterReport={this.props.venActions.sendRegisterReport}
                                            cancelRequestReportSubscription={this.props.venActions.cancelRequestReportSubscription}

                                            
                                             />
                                          
        
                       </TabContainer> }
      { value === 2 && <TabContainer>
                       </TabContainer> }
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
