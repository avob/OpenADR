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
        marginTop: 22

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

export class VenDetailPage extends React.Component {
  
  constructor() {
    super();
    this.state = {
      value: 0,
      venId: null
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
          history.push("/ven/detail/"+this.props.match.params.username+"/requests")
          break;
      case 3:
        history.push("/ven/detail/"+this.props.match.params.username+"/optschedules")
        break;
      case 4:
        history.push("/ven/detail/"+this.props.match.params.username+"/enrollments")
        break;
      case 5:
        history.push("/ven/detail/"+this.props.match.params.username+"/groups")
        break;
      default:
        break;
    }
  };


  componentDidMount() {
	  
    this.props.vtnConfigurationActions.loadMarketContext();
    this.props.vtnConfigurationActions.loadGroup();
    this.props.venActions.loadVenDetail( this.props.match.params.username );
    this.props.venActions.loadVenGroup( this.props.match.params.username );
    this.props.venActions.loadVenMarketContext( this.props.match.params.username );
//    this.props.venActions.loadVenAvailableReport( this.props.match.params.username );
    this.props.venActions.loadVenRequestedReport( this.props.match.params.username );
    this.props.venActions.loadVenOpt( this.props.match.params.username );
    this.setState({venId: this.props.match.params.username})
    
    switch(this.props.match.params.panel){
      case "settings":
        this.setState({value:0});
        break;
      case "reports":
        this.setState({value:1});
        break;
      case "requests":
          this.setState({value:2});
          break;
      case "optschedules":
        this.setState({value:3});
        break;
      case "enrollments":
        this.setState({value:4});
        break;
      case "groups":
        this.setState({value:5});
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
        <Tab label="Settings" />
        <Tab label="Reports" />
    	<Tab label="Requests" />
    	<Tab label="OptSchedules" />
        <Tab label="Enrollments" />
        <Tab label="Groups" />
      </Tabs>
      <Divider variant="middle" />
      { value === 0 && <TabContainer>
                         <VenDetailSettings classes={ classes }
                                            ven={ ven_detail.ven }

                                            group={ ven_detail.group }
                                            venGroup={ ven_detail.venGroup }
                                            
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
                        					totalReport={ven_detail.totalReport}
                        					totalPageReport={ven_detail.totalPageReport}
                                            requestRegisterReport={this.props.venActions.requestRegisterReport}
                                            sendRegisterReport={this.props.venActions.sendRegisterReport}
                                            cancelRequestReportSubscription={this.props.venActions.cancelRequestReportSubscription}

                        					pageVenAvailableReport={this.props.venActions.pageVenAvailableReport}
                        					venId={this.state.venId}
                                             />
                                          
        
                       </TabContainer> }
      
      { value === 2 && <TabContainer>
					      <VenDetailRequest classes={ classes }
					                          ven={ ven_detail.ven }
					                          marketContext={ ven_detail.marketContext }
					                          availableReport={ven_detail.availableReport}
					                          requestedReport={ven_detail.requestedReport}
											      totalRequest={ven_detail.totalRequest}
						      					totalPageRequest={ven_detail.totalPageRequest}
					                          requestRegisterReport={this.props.venActions.requestRegisterReport}
					                          sendRegisterReport={this.props.venActions.sendRegisterReport}
					                          cancelRequestReportSubscription={this.props.venActions.cancelRequestReportSubscription}
					
					      						pageVenRequestedReport={this.props.venActions.pageVenRequestedReport}
					      venId={this.state.venId}
					                           />
					                        
					
					     </TabContainer> }
      
      { value === 3 && <TabContainer>
                      <VenDetailOptSchedule classes={ classes }
                                            ven={ ven_detail.ven }
                                            venOpt={ven_detail.venOpt}
                                            marketContext={ ven_detail.marketContext }                                     
                                             />


        
                       </TabContainer> }
      { value === 4 && <TabContainer>
                      <VenDetailEnrollment classes={ classes }
                                            ven={ ven_detail.ven }
                                            marketContext={ ven_detail.marketContext }
                                            venMarketContext={ ven_detail.venMarketContext }  

                                            addVenMarketContext={ this.props.venActions.addVenMarketContext }
                                            removeVenMarketContext={ this.props.venActions.removeVenMarketContext }                                    
                                             />


        
                       </TabContainer> }
      { value === 5 && <TabContainer>
                      <VenDetailGroup classes={ classes }
                                            ven={ ven_detail.ven }
                                            

                                            group={ ven_detail.group }
                                            venGroup={ ven_detail.venGroup }

                                            addVenGroup={ this.props.venActions.addVenGroup }
                                            removeVenGroup={ this.props.venActions.removeVenGroup }                                  
                                             />


        
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
