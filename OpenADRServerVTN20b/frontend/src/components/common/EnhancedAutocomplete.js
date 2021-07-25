import React from 'react';

import TextField from '@material-ui/core/TextField';

import Autocomplete, { createFilterOptions } from '@material-ui/lab/Autocomplete';

const filter = createFilterOptions();

var EnhancedAutocomplete = (props) => {
  const [value, setValue] = React.useState(null);
  const {options, label, creatable, onSelected} = props;
  return (
    <Autocomplete
      disabled={props.disabled ? props.disabled : false}
      value={props.value}
      onChange={(event, newValue) => {
        if (typeof newValue === 'string') {
          setValue({
            title: newValue,
          });
          onSelected(newValue);
        } else if (newValue && newValue.inputValue) {
          // Create a new value from the user input
          setValue({
            title: newValue.inputValue,
          });
          onSelected(newValue.inputValue);
        } else {
          setValue(newValue);
          onSelected(newValue);
        }
      }}
      filterOptions={(options, params) => {
        const filtered = filter(options, params);

        // Suggest the creation of a new value
        if (creatable && params.inputValue !== '') {
          filtered.push({
            inputValue: params.inputValue,
            title: `Add "${params.inputValue}"`,
          });
        }

        return filtered;
      }}
      selectOnFocus
      clearOnBlur
      handleHomeEndKeys
      options={options}
      getOptionLabel={(option) => {
        // Value selected with enter, right from the input
        if (typeof option === 'string') {
          return option;
        }
        // Add "xxx" option created dynamically
        if (option.inputValue) {
          return option.inputValue;
        }

        // Regular option
        return option.title;
      }}
      renderOption={(option) => (option.title) ? option.title : option}
      freeSolo
      renderInput={(params) => (
        <TextField {...params} label={label} />
      )}
    />
  );
}

export default EnhancedAutocomplete;