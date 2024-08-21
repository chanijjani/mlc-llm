/*
 * Copyright 2024 ObjectBox Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// automatically generated by the FlatBuffers compiler, do not modify

package io.objectbox.query;

/**
 * Not really an enum, but binary flags to use across languages
 */
@SuppressWarnings("unused")
public final class OrderFlags {
  private OrderFlags() { }
  /**
   * Reverts the order from ascending (default) to descending.
   */
  public static final int DESCENDING = 1;
  /**
   * Makes upper case letters (e.g. "Z") be sorted before lower case letters (e.g. "a").
   * If not specified, the default is case insensitive for ASCII characters.
   */
  public static final int CASE_SENSITIVE = 2;
  /**
   * For scalars only: changes the comparison to unsigned (default is signed).
   */
  public static final int UNSIGNED = 4;
  /**
   * null values will be put last.
   * If not specified, by default null values will be put first.
   */
  public static final int NULLS_LAST = 8;
  /**
   * null values should be treated equal to zero (scalars only).
   */
  public static final int NULLS_ZERO = 16;
}

