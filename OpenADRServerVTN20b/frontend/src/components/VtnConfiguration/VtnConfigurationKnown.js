import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';

import * as accountActions from '../../actions/accountActions';

import { withStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';

import amber from '@material-ui/core/colors/amber';

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Toolbar from '@material-ui/core/Toolbar';

import { history } from '../../store/configureStore';

var KnownUnitTable = (props) => {
  const {classes, unit} = props
  return (
    <div className={classes.root}>
      <Toolbar
      className={classes.root}
    >
      <div className={classes.title} style={{ flex: 1 }}>
         <Typography variant="h6" id="tableTitle">
             Units
          </Typography>
      </div>
    </Toolbar>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Item description</TableCell>
            <TableCell>Item units</TableCell>
            <TableCell>Type</TableCell>
            <TableCell></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {unit && unit.map(row => (
            <TableRow key={row.itemDescription+ row.itemUnits+ row.xmlType}>
              <TableCell>{row.itemDescription}</TableCell>
              <TableCell>{row.itemUnits}</TableCell>
              <TableCell>{row.xmlType}</TableCell>

            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )

}

var KnownSignalTable = (props) => {
  const {classes, signal} = props
  return (
    <div className={classes.root}>
      <Toolbar
      className={classes.root}
    >
      <div className={classes.title} style={{ flex: 1 }}>
         <Typography variant="h6" id="tableTitle">
             Signals
          </Typography>
      </div>
    </Toolbar>
      <Table className={classes.table}>
        <TableHead>
          <TableRow>
            <TableCell>Signal name</TableCell>
            <TableCell>Signal type</TableCell>
            <TableCell>Signal unit</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {signal && signal.map(row => (
            <TableRow key={row.signalName+row.signalType}>
              <TableCell>{row.signalName}</TableCell>
              <TableCell>{row.signalType}</TableCell>
              <TableCell>{row.units && row.units.map(n => {return <div>{n.itemDescription + "-" + n.itemUnits}</div>})}</TableCell>



            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )

}


function TabContainer( props ) {
  return (
  <Typography component="div" style={ { padding: 8 * 3 } }>
    { props.children }
  </Typography>
  );
}

export class VtnConfigurationKnown extends React.Component {
  state = {
    value: 0,
  };

  handleChange = (event, value) => {
    this.setState( {
      value
    } );
    switch(value) {
      case 0:
        history.push("/vtn_configuration/known/unit")
        break;
      case 1:
        history.push("/vtn_configuration/known/signal")
        break;
      default:
        break;
    }
  };


  componentDidMount() {
    switch(this.props.panel){
      case "unit":
        this.setState({value:0});
        break;
      case "signal":
        this.setState({value:1});
        break;
      default:
        break;
    }
  }


  render() {
    const {classes, known} = this.props;
    const {value} = this.state;
    return (
    <div className={ classes.root }>
      <Tabs value={ this.state.value }
            onChange={ this.handleChange }
            indicatorColor="primary"
            textColor="primary"
            centered>
        <Tab label="Units" />
        <Tab label="Signals" />
      </Tabs>
      <Divider variant="middle" />
      { value === 0 && <KnownUnitTable classes={classes} unit={known.unit}/>
                        }
      { value === 1 && <KnownSignalTable classes={classes} signal={known.signal}/>
                       }
    </div>

    );
  }
}

export default VtnConfigurationKnown;
