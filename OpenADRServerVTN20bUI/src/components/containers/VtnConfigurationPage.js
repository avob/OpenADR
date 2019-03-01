import React from 'react';
import PropTypes from 'prop-types';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as actions from '../../actions/vtnConfigurationActions';
import VtnConfigurationParameter from '../VtnConfiguration/VtnConfigurationParameter';
import VtnConfigurationMarketContext from '../VtnConfiguration/VtnConfigurationMarketContext';

import { withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';

function TabContainer(props) {
  return (
    <Typography component="div" style={{ padding: 8 * 3 }}>
      {props.children}
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
    maxWidth: 300,
    minWidth: 300,
  },
  media: {
    height: 40,
  },
  button: {
    margin: theme.spacing.unit,
  },
});

export class VtnConfigurationPage extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState({ value });
  };


  componentDidMount() {
    this.props.actions.loadVtnConfiguration();
    this.props.actions.loadMarketContext();
  }

  render() {
    const { classes, vtnConfiguration } = this.props;
    const { value } = this.state;
    return (
      <Paper className={classes.root}>
        <Tabs
          value={this.state.value}
          onChange={this.handleChange}
          indicatorColor="primary"
          textColor="primary"
          centered
        >
          <Tab label="Market Contexts" />           
          <Tab label="Targets" />
          <Tab label="Units" />
          <Tab label="Parameters" /> 
        </Tabs>
        
        {value === 0 && <TabContainer>
          <VtnConfigurationMarketContext classes={classes} marketContext={vtnConfiguration.marketContext} createMarketContext={this.props.actions.createMarketContext}/>
        </TabContainer>}
        {value === 1 && <TabContainer></TabContainer>}
        {value === 2 && <TabContainer></TabContainer>}
        {value === 3 && <TabContainer><VtnConfigurationParameter classes={classes} vtnConfiguration={vtnConfiguration.parameters}/></TabContainer>}
      </Paper>
     
    );
  }
}

VtnConfigurationPage.propTypes = {
  actions: PropTypes.object.isRequired
};

function mapStateToProps(state) {
  return {
    vtnConfiguration: state.vtnConfiguration
  };
}

function mapDispatchToProps(dispatch) {
  return {
    actions: bindActionCreators(actions, dispatch)
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(withStyles(styles)(VtnConfigurationPage));
