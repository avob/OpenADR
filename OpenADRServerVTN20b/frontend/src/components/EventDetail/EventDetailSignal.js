import React from 'react';
import Divider from '@material-ui/core/Divider';
import Grid from '@material-ui/core/Grid';
import {EventSignalPanel} from '../common/EventSignalPanel'
import DetailedSignalTable from '../common/DetailedSignalTable'
import DetailedBaselineTable from '../common/DetailedBaselineTable'
import TargetTable from '../common/TargetTable'
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';

import {Panel} from '../common/Structure'

export class EventDetailSignal extends React.Component {




  handleAddSignalClick = () => {
    this.props.addCopySignals();
  }

  render() {
    const {classes, event} = this.props;
    var that = this;
    var hasError = false;
    var signals = event.signals;
    var baseline = event.baseline;
    var targets = event.targets;

    return (
    <div className={ classes.root } >
         
          
          <Panel classes={classes}  title="Signals">
            <DetailedSignalTable classes={classes} signal={signals}/>
          </Panel>
          <Panel classes={classes}  title="Baseline">
            <DetailedBaselineTable classes={classes} baseline={baseline}/>
          </Panel>
          <Panel classes={classes}  title="Targets">
            <TargetTable classes={classes} targets={targets}/>
          </Panel>
         


    </div>
    );
  }
}

export default EventDetailSignal;

