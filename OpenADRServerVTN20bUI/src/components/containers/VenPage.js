import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as actions from '../../actions/venActions';
import VenList from '../Ven/VenList'
import { withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
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

  input: {
    marginLeft: 8,
    flex: 1,
  },
  iconButton: {
    padding: 10,
  },
});

export class VenPage extends React.Component {

  componentDidMount() {
    this.props.actions.loadVen();
  }

  render() {
    const {classes, ven} = this.props;
    return (
    <div className={ classes.root }>
      <Tabs value={ 0 }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="VENs" />
      </Tabs>
      <Divider variant="middle" />
      <Typography component="div" style={ { padding: 8 * 3 } }>
        <VenList classes={ classes }
                 ven={ ven.ven }
                 deleteVen={ this.props.actions.deleteVen } />
      </Typography>
    </div>

    );
  }
}

VenPage.propTypes = {
  actions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    ven: state.ven
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
)( withStyles( styles )( VenPage ) );
