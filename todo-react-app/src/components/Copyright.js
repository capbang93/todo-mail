import { Typography } from '@material-ui/core'
import React from 'react'

function Copyright() {
  return (
    <Typography variant='body2' color='textSecondary' align='center'>
        {"Copyright C "}
        fsoftwareengineer, {new Date().getFullYear()}
        {"."}
    </Typography>
  )
}

export default Copyright