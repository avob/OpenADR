import React from 'react';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import Toolbar from '@material-ui/core/Toolbar';
import Button from '@material-ui/core/Button';

import UnitPanel from '../common/UnitPanel'

import TextField from '@material-ui/core/TextField';

import { MarketContextSelectDialog, GroupSelectDialog, VenStatusSelectDialog
  , VenSelectDialog, EventStatusSelectDialog, TargetSelectDialog } from './VtnconfigurationDialog'


var DetailedTargetTable = (props) => {
  const {classes, targets, onTargetChange, ven, group} = props
   const [targetSelectOpen, setTargetSelectOpen] = React.useState(false);
   const [venSelectOpen, setVenSelectOpen] = React.useState(false);
   const [groupSelectOpen, setGroupSelectOpen] = React.useState(false);
   const [currentIndex, setCurrentIndex] = React.useState(0);



  var onTargetSelect = (targetType) => {
    if(targets.length - 1 < currentIndex ) {
      targets.push({
        targetType: "",
        targetId: ""
      });
    } 
    targets[currentIndex].targetType = targetType;
    targets[currentIndex].targetType = "";
    onTargetTypeChange(currentIndex)(targetType)
  }

  var onRemoveTarget = (index) => {
      var newTarget = [];
      targets && targets.map((target, i) => {
        if(index != i) {
          newTarget.push(target);
        }
      });
      onTargetChange(newTarget);
  }

  var onTargetTypeChange = (index) => {
    return (targetType) => {
   
        targets[index].targetType = targetType;
        onTargetChange(targets);
        setCurrentIndex(index);
        switch(targetType) {
            case "ven":
              setVenSelectOpen(true);
              break;
            case "group":
              setGroupSelectOpen(true);
              break;

        }
        
    }
  }

  var onTargetIdChange = (index) => {
    return (targetId) => {
        targets[index].targetId = targetId;
        onTargetChange(targets);
    }
  }

  var onVenSelect = (ven) => {
      onTargetIdChange(currentIndex)(ven.username)
      setVenSelectOpen(false);

  }

   var onGroupSelect = (group) => {
      onTargetIdChange(currentIndex)(group.name)
      setGroupSelectOpen(false);

  }



  return (
      <Table className={classes.table}>
        <TargetSelectDialog open={targetSelectOpen} close={(targetType) => {onTargetSelect(targetType); setTargetSelectOpen(false)}}/>
        <VenSelectDialog open={ venSelectOpen }
                        suggestions={ven}
                        onSuggestionsFetchRequested={props.onVenSuggestionsFetchRequested}
                        onSuggestionsClearRequested={props.onVenSuggestionsClearRequested}
                        onSuggestionsSelect={onVenSelect}
                        close={ onVenSelect }
                        title="Select VEN to target" />

        <GroupSelectDialog open={groupSelectOpen} group={group} close={onGroupSelect}/>
         
        <TableHead>
          <TableRow key={"header"}>
            <TableCell>Type</TableCell>
            <TableCell>Target</TableCell>
            <TableCell>
              <Button key="btn_delete"
                    color="primary"
                    size="small"
                    onClick={ () => {
                      setTargetSelectOpen(true)
                      setCurrentIndex(targets.length);
                    } }>
              Add Target
            </Button>
            </TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {targets && targets.map((row, index) => (
            <TableRow key={index}>
              <TableCell>
                <TextField required
                      label="Target type"
                      value={row.targetType}
                      onClick={(e) => {setCurrentIndex(index); setTargetSelectOpen(true)}}
                      InputLabelProps={{
                        shrink: true,
                      }}    
                    />
              </TableCell>
              <TableCell>
                <TextField required
                      label="Target id"
                      value={row.targetId}
                      onClick={(e) => {onTargetTypeChange(index)(targets[index].targetType)}}
                      InputLabelProps={{
                        shrink: true,
                      }}    
                    />

              </TableCell>
              <TableCell>
              {(targets.length > 1) && 
                  <Button key="btn_delete"
                          color="secondary"
                          size="small"
                          onClick={ () => onRemoveTarget(index) }>
                    Remove Target
                  </Button>
              }
              
            </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
  )

}

export default DetailedTargetTable;