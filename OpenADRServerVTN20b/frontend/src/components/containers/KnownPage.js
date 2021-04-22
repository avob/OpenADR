import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as knownActions from '../../actions/knownActions';

import KnownList from '../Known/KnownList';

import { withStyles } from '@material-ui/core/styles';

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
  iconButton: {
    marginTop: 10
  },
});

export class KnownPage extends React.Component {
  state = {
  };

  componentDidMount() {
    this.props.knownActions.searchKnownUnit({filter:[]});
    this.props.knownActions.searchKnownSignal({filter:[]});
    this.props.knownActions.searchKnownReport({filter:[]});
  }


  render() {
    const {classes, known} = this.props;
    return (
    <div className={ classes.root }>

      <KnownList classes={ classes }
                          unit={ known.unit }
                          signal={ known.signal }
                          report={ known.report }
                           />  
                       
    </div>

    );
  }
}

KnownPage.propTypes = {
  knownActions: PropTypes.object.isRequired
};

function mapStateToProps( state ) {
  return {
    known: state.known
  };
}

function mapDispatchToProps( dispatch ) {
  return {
    knownActions: bindActionCreators( knownActions, dispatch )
  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( KnownPage ) );
