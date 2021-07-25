import React from 'react';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import Toolbar from '@material-ui/core/Toolbar';

export var H1 = ( props ) => {
  return (
        <Typography variant="h5">
           {props.value}
        </Typography>

  );
}

export var H2 = ( props ) => {
  return (
        <Typography variant="h6">
           {props.value}
        </Typography>

  );
}

export var Panel = ( props ) => {
  return (
     <Box >
      {props.title ? <Toolbar><H2 classes={props.classes}  value={props.title}/></Toolbar> : null}
      {props.children}
      </Box>

  );
}



