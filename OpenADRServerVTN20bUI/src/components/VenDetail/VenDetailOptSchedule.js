import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';


import { withStyles, MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';

import { VtnConfigurationVenCard, VtnConfigurationMarketContextCard, VtnConfigurationGroupCard } from '../common/VtnConfigurationCard'
import { MarketContextSelectDialog, GroupSelectDialog } from '../common/VtnconfigurationDialog'

import Input from '@material-ui/core/Input';
import InputBase from '@material-ui/core/InputBase';
import InputLabel from '@material-ui/core/InputLabel';
import TextField from '@material-ui/core/TextField';
import FormControl from '@material-ui/core/FormControl';
import Grid from '@material-ui/core/Grid';


import Typography from '@material-ui/core/Typography';

import Divider from '@material-ui/core/Divider';


import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';
import GridListTileBar from '@material-ui/core/GridListTileBar';

import IconButton from '@material-ui/core/IconButton';

import StarBorderIcon from '@material-ui/icons/StarBorder';

import ChipInput from 'material-ui-chip-input'

import Button from '@material-ui/core/Button';
import AddIcon from '@material-ui/icons/Add';
import RemoveIcon from '@material-ui/icons/Remove';
import SearchIcon from '@material-ui/icons/Search';


import SnackbarContent from '@material-ui/core/SnackbarContent';
import DoneIcon from '@material-ui/icons/Done';
import CloseIcon from '@material-ui/icons/Close';
import CloudDownloadIcon from '@material-ui/icons/CloudDownload';

import VenDetailHeader from './VenDetailHeader'

import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Paper from '@material-ui/core/Paper';


import { history } from '../../store/configureStore';

import {iCalDurationInSeconds, formatTimestamp} from '../../utils/time'
import {isActionReport, isHistoryReport, isTelemetryReport} from '../../utils/venReport'


export class VenDetailOptSchedule extends React.Component {
  constructor( props ) {
    super( props );
    this.state = {}
  }

  render() {
    const {classes, ven, availableReport, requestedReport, cancelRequestReportSubscription} = this.props;


    return (
    <div className={ classes.root } >
      <VenDetailHeader classes={classes} ven={ven} actions={[
 
      ]
      }/>
      <Divider style={ { marginBottom: '30px', marginTop: '20px' } } />

    </div>
    );
  }
}

export default VenDetailOptSchedule;
