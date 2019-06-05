// generated by jsonenums -type=Kind; DO NOT EDIT

package sidecar

import (
	"encoding/json"
	"fmt"
)

var (
	_KindNameToValue = map[string]Kind{
		"cleanup":         cleanup,
		"upgradesstables": upgradesstables,
		"decommission":    decommission,
		"backup":          backup,
	}

	_KindValueToName = map[Kind]string{
		cleanup:         "cleanup",
		upgradesstables: "upgradesstables",
		decommission:    "decommission",
		backup:          "backup",
	}
)

func init() {
	var v Kind
	if _, ok := interface{}(v).(fmt.Stringer); ok {
		_KindNameToValue = map[string]Kind{
			interface{}(cleanup).(fmt.Stringer).String():         cleanup,
			interface{}(upgradesstables).(fmt.Stringer).String(): upgradesstables,
			interface{}(decommission).(fmt.Stringer).String():    decommission,
			interface{}(backup).(fmt.Stringer).String():          backup,
		}
	}
}

// MarshalJSON is generated so Kind satisfies json.Marshaler.
func (r Kind) MarshalJSON() ([]byte, error) {
	if s, ok := interface{}(r).(fmt.Stringer); ok {
		return json.Marshal(s.String())
	}
	s, ok := _KindValueToName[r]
	if !ok {
		return nil, fmt.Errorf("invalid Kind: %d", r)
	}
	return json.Marshal(s)
}

// UnmarshalJSON is generated so Kind satisfies json.Unmarshaler.
func (r *Kind) UnmarshalJSON(data []byte) error {
	var s string
	if err := json.Unmarshal(data, &s); err != nil {
		return fmt.Errorf("Kind should be a string, got %s", data)
	}
	v, ok := _KindNameToValue[s]
	if !ok {
		return fmt.Errorf("invalid Kind %q", s)
	}
	*r = v
	return nil
}
