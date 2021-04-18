import React from 'react';
import { connect } from 'react-redux';

import { withStyles } from '@material-ui/core/styles';
import Paper from '@material-ui/core/Paper';
import SwaggerUI from "swagger-ui-react"
import "swagger-ui-react/swagger-ui.css"

import { config } from '../../store/configureStore';

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

export class SwaggerUIPage extends React.Component {

  render() {
    const {classes} = this.props;
    return (
    <Paper className={ classes.root } >
       	<SwaggerUI url={config.vtnSwaggerUrl} /> 
    </Paper>

    );
  }
}

SwaggerUIPage.propTypes = {

};

function mapStateToProps( state ) {
  return {

  };
}

function mapDispatchToProps( dispatch ) {
  return {

  };
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)( withStyles( styles )( SwaggerUIPage ) );
