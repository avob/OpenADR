import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as actions from '../../actions/venActions';

import { withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';

import VenDetailSettings from '../VenDetail/VenDetailSettings'


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
});

export class VenDetailPage extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
  };


  componentDidMount() {
    this.props.actions.loadVenDetail( this.props.match.params.username );
  }

  render() {
    const {classes, ven_detail} = this.props;
    const {value} = this.state;
    console.log( ven_detail );
    return (
    <div className={ classes.root }>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="VEN Settings" />
        <Tab label="Registrations" />
        <Tab label="Subscriptions" />
        <Tab label="Reports" />
        <Tab label="OptSchedules" />
      </Tabs>
      <Divider variant="middle" />
      { value === 0 && <TabContainer>
                         <VenDetailSettings />
                       </TabContainer> }
      { value === 1 && <TabContainer>
                       </TabContainer> }
      { value === 2 && <TabContainer>
                       </TabContainer> }
      { value === 3 && <TabContainer>
                       </TabContainer> }
      { value === 4 && <TabContainer>
                       </TabContainer> }
    </div>

    );
  }
}

VenDetailPage.propTypes = {
  actions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    ven_detail: state.ven_detail
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
)( withStyles( styles )( VenDetailPage ) );
