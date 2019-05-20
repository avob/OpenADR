import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as actions from '../../actions/vtnConfigurationActions';
import VtnConfigurationParameter from '../VtnConfiguration/VtnConfigurationParameter';
import VtnConfigurationMarketContext from '../VtnConfiguration/VtnConfigurationMarketContext';
import VtnConfigurationGroup from '../VtnConfiguration/VtnConfigurationGroup';

import { withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';

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
  iconButton: {
    marginTop: 10
  },
});

export class VtnConfigurationPage extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
    switch(value) {
      case 0:
        history.push("/vtn_configuration/marketcontext")
        break;
      case 1:
        history.push("/vtn_configuration/group")
        break;
      case 2:
        history.push("/vtn_configuration/parameter")
        break;
      default:
        break;
    }
  };


  componentDidMount() {
    this.props.actions.loadVtnConfiguration();
    this.props.actions.loadMarketContext();
    this.props.actions.loadGroup();
    switch(this.props.match.params.panel){
      case "marketcontext":
        this.setState({value:0});
        break;
      case "group":
        this.setState({value:1});
        break;
      case "parameter":
        this.setState({value:2});
        break;
      default:
        break;
    }
  }

  render() {
    const {classes, vtnConfiguration} = this.props;
    const {value} = this.state;
    return (
    <div className={ classes.root }>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="Market Contexts" />
        <Tab label="Groups" />
        <Tab label="Parameters" />
      </Tabs>
      <Divider variant="middle" />
      { value === 0 && <TabContainer>
                         <VtnConfigurationMarketContext classes={ classes }
                                                        marketContext={ vtnConfiguration.marketContext }
                                                        createMarketContext={ this.props.actions.createMarketContext }
                                                        updateMarketContext={ this.props.actions.updateMarketContext }
                                                        deleteMarketContext={ this.props.actions.deleteMarketContext } />
                       </TabContainer> }
      { value === 1 && <TabContainer>
                         <VtnConfigurationGroup classes={ classes }
                                                group={ vtnConfiguration.group }
                                                createGroup={ this.props.actions.createGroup }
                                                updateGroup={ this.props.actions.updateGroup }
                                                deleteGroup={ this.props.actions.deleteGroup } />
                       </TabContainer> }
      { value === 2 && <TabContainer>
                         <VtnConfigurationParameter classes={ classes } vtnConfiguration={ vtnConfiguration.parameters } />
                       </TabContainer> }
    </div>

    );
  }
}

VtnConfigurationPage.propTypes = {
  actions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    vtnConfiguration: state.vtnConfiguration
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    actions: bindActionCreators( actions, dispatch )
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( VtnConfigurationPage ) );
