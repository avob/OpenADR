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
import {H1} from '../common/Structure'




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
      venId: null,
      dialogOpen: false,
      selectedReportSpecifierId: null
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
    console.log(this.props.match.params.panel)
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

  handleEventClick (ven) {
      history.push( '/event', { filters: [{type:"VEN", value: ven.username}] });
    }

  handleActionClick (ven) {
      this.setState({dialogOpen: true})
    }

  render() {
    const {classes, ven_detail} = this.props;
    const {value} = this.state;
    return (
    <div className={ classes.root }>
    <Paper>
    <Toolbar>
            <div style={{flex: '0 0 auto'}}>
              <H1 value={ven_detail.ven.commonName} />
            </div>
            <div style={{flex: '1 1 100%'}} />
            <div style={{ flex: '0 0 auto'}}>


                
                   <Button size="small" color="primary" onClick={() => {this.handleEventClick(ven_detail.ven) }}>Event</Button>
                   <Button size="small" color="primary" onClick={() => {this.handleActionClick(ven_detail.ven) }}>Action</Button>
            </div>
                


        </Toolbar>
            <Divider/>

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
      <Divider/>
      { value === 0 && 
                         <VenDetailSettings classes={ classes }
                                            ven={ ven_detail.ven }
                                            venActions={ this.props.venActions }

                                            group={ ven_detail.group }
                                            venGroup={ ven_detail.venGroup }
                                            
                                            registerPartyRequestReregistration={this.props.venActions.registerPartyRequestReregistration}
                                            registerPartyCancelPartyRegistration={this.props.venActions.registerPartyCancelPartyRegistration}
                                            cleanRegistration={this.props.venActions.cleanRegistration}

                                             />
                                            
                                            
                     }
      { value === 1 && !this.props.match.params.reportSpecifierId && <VenDetailReport classes={ classes }
                                            ven={ ven_detail.ven }
                                            venActions={ this.props.venActions }
                                            marketContext={ ven_detail.marketContext }
                                            availableReport={ven_detail.availableReport}
                        					totalReport={ven_detail.totalReport}
                        					totalPageReport={ven_detail.totalPageReport}
                                            requestRegisterReport={this.props.venActions.requestRegisterReport}
                                            sendRegisterReport={this.props.venActions.sendRegisterReport}
                                            cancelRequestReportSubscription={this.props.venActions.cancelRequestReportSubscription}

                        					pageVenAvailableReport={this.props.venActions.pageVenAvailableReport}
                        					venId={this.state.venId}

                                  viewDescription={(reportSpecifierId) => {this.setState({selectedReportSpecifierId: reportSpecifierId, value:6})}}
                                             /> }

      { value === 1 && this.props.match.params.reportSpecifierId && <VenDetailReportDescription classes={ classes }
                                            ven={ ven_detail.ven }
                                            venActions={ this.props.venActions }
                                             availableReportDescription={ven_detail.availableReportDescription}
                                             selectedReportSpecifierId={this.props.match.params.reportSpecifierId }
                                             /> }
      
      { value === 2 && !this.props.match.params.reportRequestId && <VenDetailRequest classes={ classes }
					                          ven={ ven_detail.ven }
                                    venActions={ this.props.venActions }
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

					                           />		}

      { value === 2 && this.props.match.params.reportRequestId && <VenDetailRequestSpecifier classes={ classes }
                                    ven={ ven_detail.ven }
                                    venActions={ this.props.venActions }
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
                                    selectedReportRequestId={this.props.match.params.reportRequestId}
                                    requestedReportSpecifier={ven_detail.requestedReportSpecifier}
                                     />    }

      
      { value === 3 && <VenDetailOptSchedule classes={ classes }
                                            ven={ ven_detail.ven }
                                            venOpt={ven_detail.venOpt}
                                            marketContext={ ven_detail.marketContext }                                     
                                             />}
      { value === 4 && <VenDetailEnrollment classes={ classes }
                                            ven={ ven_detail.ven }
                                            venActions={ this.props.venActions }
                                            marketContext={ ven_detail.marketContext }
                                            venMarketContext={ ven_detail.venMarketContext }  

                                            addVenMarketContext={ this.props.venActions.addVenMarketContext }
                                            removeVenMarketContext={ this.props.venActions.removeVenMarketContext }                                    
                                             />}
      { value === 5 && <VenDetailGroup classes={ classes }
                                            ven={ ven_detail.ven }
                                            venActions={ this.props.venActions }

                                            group={ ven_detail.group }
                                            venGroup={ ven_detail.venGroup }

                                            addVenGroup={ this.props.venActions.addVenGroup }
                                            removeVenGroup={ this.props.venActions.removeVenGroup }                                  
                                             /> }



                                             
      </Paper>
      <VenActionDialog open={ this.state.dialogOpen }
             close={ this.handleDialogClose }
             title="Ven actions" 
             ven={ven_detail.ven}
             venActions={this.props.venActions}/>
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
