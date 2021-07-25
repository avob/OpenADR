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

import MarketContextDetailSignal from '../MarketContextDetail/MarketContextDetailSignal'
import MarketContextDetailSettings from '../MarketContextDetail/MarketContextDetailSettings'
import MarketContextDetailReport from '../MarketContextDetail/MarketContextDetailReport'








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
    };

    
  }

  handleDialogOpen = () => {
    this.setState({dialogOpen: true})
  }

  handleDialogClose = () => {
    this.setState({dialogOpen: false})
  }

  handleChange = (marketcontext, value) => {
    this.setState( {
      value
    } );
    switch(value) {
      case 0:
        history.push("/marketcontext/detail/"+this.props.match.params.name+"/settings")
        break;
      case 1:
        history.push("/marketcontext/detail/"+this.props.match.params.name+"/signal")
        break;
      case 2:
        history.push("/marketcontext/detail/"+this.props.match.params.name+"/report")
        break;
      default:
        break;
    }
  };


  componentDidMount() {

    this.props.vtnConfigurationActions.getMarketContext(this.props.match.params.name);
	  

    switch(this.props.match.params.panel){
      case "settings":
        this.setState({value:0});
        break;
      case "signal":
        this.setState({value:1});
        break;
      case "report":
        this.setState({value:2});
        break;
      default:
        this.setState({value:0});
        break;
    }
  }

  handleEventClick (marketContext) {
      history.push( '/event', { filters: [{type:"MARKET_CONTEXT", value: marketContext.name}] });
    }

  handleVenClick (marketContext) {
    history.push( '/ven', { filters: [{type:"MARKET_CONTEXT", value: marketContext.name}] });
  }

  handleEditClick (marketContext) {
    history.push( '/marketcontext/update/'+marketContext.name);
  }

  render() {
    const {classes, marketContextDetail} = this.props;
    const {value} = this.state;
    return (
    <div className={ classes.root }>
    <Paper>
    <Toolbar>
            <div style={{flex: '0 0 auto'}}>
              <H1 value={marketContextDetail && marketContextDetail.marketContext.name} />
            </div>
            <div style={{flex: '1 1 100%'}} />
            <div style={{ flex: '0 0 auto'}}>
              <Button size="small" color="primary" onClick={() => {this.handleEventClick(marketContextDetail.marketContext) }}>Event</Button>
              <Button size="small" color="primary" onClick={() => {this.handleVenClick(marketContextDetail.marketContext) }}>VEN</Button>
              <Button size="small" color="primary" onClick={() => {this.handleEditClick(marketContextDetail.marketContext) }}>Edit</Button>
            </div>
               

        </Toolbar>
      <Divider/>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="Settings" />
        <Tab label="Signals" />
    	  <Tab label="Reports" />
    	
      </Tabs>
      <Divider/>

      { marketContextDetail.marketContext && value === 0 && 
                         <MarketContextDetailSettings classes={ classes }
                                          marketContext={marketContextDetail.marketContext}

                                             />
                                            
                                            
                     }



      { marketContextDetail.marketContext.signals && value === 1 && 
                         <MarketContextDetailSignal classes={ classes }
                                          marketContext={marketContextDetail.marketContext}

                                             />

                     
                     }

      { marketContextDetail.marketContext.reports && value === 2 && 
                         <MarketContextDetailReport classes={ classes }
                                          marketContext={marketContextDetail.marketContext}

                                             />

                     
                     }
                     
                                             
      </Paper>

    </div>

    );
  }
}

VenDetailPage.propTypes = {
  vtnConfigurationActions: PropTypes.object.isRequired,


};

function mapStateToProps( state ) {
  return {
    marketContextDetail: state.marketContextDetail
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    vtnConfigurationActions: bindActionCreators( vtnConfigurationActions, dispatch ),
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( VenDetailPage ) );
