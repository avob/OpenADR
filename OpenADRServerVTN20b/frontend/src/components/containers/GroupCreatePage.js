import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as vtnConfigurationActions from '../../actions/vtnConfigurationActions';
import * as venActions from '../../actions/venActions';

import { withStyles } from '@material-ui/core/styles';
import Divider from '@material-ui/core/Divider';


import green from '@material-ui/core/colors/green';

import GroupCreate from '../GroupCreate/GroupCreate'

const styles = theme => ({
  root: {
    flexGrow: 1,
  },
  container: {
    display: 'flex',
    flexWrap: 'wrap',
  },
  textField: {
    marginLeft: theme.spacing(1),
    marginRight: theme.spacing(1),
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

export class GroupCreatePage extends React.Component {
  state = {
  };


  componentDidMount() {
 
 }

  render() {
    const {classes} = this.props;

    return (
    <div className={ classes.root }>
      <Divider variant="middle" />
        <GroupCreate classes={classes} 
                        createGroup={ this.props.vtnConfigurationActions.createGroup }
                        updateGroup={ this.props.vtnConfigurationActions.updateGroup }
                        deleteGroup={ this.props.vtnConfigurationActions.deleteGroup } 
                        />    
                              

    </div>

    );
  }
}

GroupCreatePage.propTypes = {
  venActions: PropTypes.object.isRequired,
  vtnConfigurationActions: PropTypes.object.isRequired,


};

function mapStateToProps( state ) {
  return {
    ven_detail_report: state.ven_detail_report
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
)( withStyles( styles )( GroupCreatePage ) );
